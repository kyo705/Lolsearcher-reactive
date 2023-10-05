package com.lolsearcher.reactive.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLockReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;

import static com.lolsearcher.reactive.config.ReactiveRedisConfig.LOCK_KEY_SUFFIX;


@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class ReactiveRedisCacheAop {

    private final CacheUtils cacheUtils;
    private final ObjectMapper objectMapper;
    private final ReactiveStringRedisTemplate reactiveRedisTemplate;
    private final RedissonReactiveClient redissonClient;

    /**
    *   Redis Cache에서 먼저 key 값에 해당하는 value를 찾는다.
    *   있을 경우 해당 value를 리턴, 없으면 실제 메소드 실행 후 Cache에 저장하고 실제 value를 리턴
     */
    @Around("@annotation(com.lolsearcher.reactive.cache.ReactiveRedisCacheable)")
    public Object lookAsideReactiveRedisCache(ProceedingJoinPoint joinPoint){

        Method method = cacheUtils.getMethod(joinPoint);
        Class<?> rawReturnType = method.getReturnType();

        ReactiveRedisCacheable annotation = method.getAnnotation(ReactiveRedisCacheable.class);

        String name = annotation.name();
        String key = cacheUtils.resolveKey(joinPoint, annotation.key());

        String compKey = cacheUtils.resolveCompKey(name, key);
        Duration time = cacheUtils.resolveTtl(annotation.ttl());

        TypeReference typeRefForMapper = cacheUtils.getTypeReference(method);

        RLockReactive lock = redissonClient.getLock(compKey + LOCK_KEY_SUFFIX);

        if (rawReturnType.isAssignableFrom(Mono.class)) {

            return lock.tryLock()
                    .flatMap(t -> lock.isLocked())
                    .doOnSuccess(isLocked -> System.out.println("lock before " + isLocked))
                    .flatMap(empty -> reactiveRedisTemplate
                            .opsForValue()
                            .get(compKey)
                            .switchIfEmpty(methodMonoResponseToCache(joinPoint, compKey, time))
                            .map(cacheResponse -> {
                                try {
                                    return objectMapper.readValue(cacheResponse, Class.forName(typeRefForMapper.getType().getTypeName()));
                                } catch (JsonProcessingException | ClassNotFoundException e) {
                                    return Mono.error(e);
                                }
                            })
                    )
                    .doFinally(result -> lock.forceUnlock().doOnNext(t -> System.out.println(t + " 언락 성공")).subscribe());

        } else if (rawReturnType.isAssignableFrom(Flux.class)) {

            return lock.isLocked()
                    .doOnSuccess(isLocked -> lock.lock().subscribe())
                    .flatMapMany(empty -> reactiveRedisTemplate
                            .opsForValue()
                            .get(compKey)
                            .switchIfEmpty(methodFluxResponseToCache(joinPoint, compKey, time))
                            .flatMapIterable(cacheResponse -> {
                                try {
                                    return objectMapper.readValue(cacheResponse, new TypeReference<List<Object>>() {
                                        @Override
                                        public Type getType() {
                                            return typeRefForMapper.getType();
                                        }
                                    });
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                    )
                    .doOnNext(result -> lock.unlock().subscribe());
        }
        throw new RuntimeException("ReactiveRedisCacheable : 매핑된 메소드의 리턴 타입이 Mono 와 Flux 외에는 지원되지 않습니다.");
    }



    private Mono<String> methodMonoResponseToCache(ProceedingJoinPoint joinPoint, String key, Duration time) {

        try {
            return ((Mono<?>) joinPoint.proceed(joinPoint.getArgs()))
                    .flatMap(methodResponse ->{
                        try {
                            String result = objectMapper.writeValueAsString((methodResponse));

                            return reactiveRedisTemplate
                                    .opsForValue()
                                    .set(key, result, time)
                                    .map(r -> result);

                        } catch (JsonProcessingException e) {
                            return Mono.error(e);
                        }
                    });
        } catch (Throwable e) {
            return Mono.error(e);
        }
    }

    private Mono<String> methodFluxResponseToCache(ProceedingJoinPoint joinPoint, String key, Duration time) {
        try {
            return ((Flux<?>) joinPoint.proceed(joinPoint.getArgs()))
                    .collectList()
                    .flatMap(methodResponseList -> {
                        try {
                            String responseList = objectMapper.writeValueAsString(methodResponseList);

                            return reactiveRedisTemplate
                                    .opsForValue()
                                    .set(key, responseList, time)
                                    .map(isSaved -> responseList);

                        } catch (JsonProcessingException e) {
                            return Mono.error(e);
                        }
                    });

        } catch (Throwable e) {
            return Mono.error(e);
        }
    }
}
