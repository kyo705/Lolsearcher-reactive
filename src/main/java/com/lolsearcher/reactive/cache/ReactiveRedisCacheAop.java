package com.lolsearcher.reactive.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class ReactiveRedisCacheAop {

    private final CacheUtils cacheUtils;
    private final ObjectMapper objectMapper;
    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

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

        String CompKey = cacheUtils.resolveCompKey(name, key);
        Duration time = cacheUtils.resolveTtl(annotation.ttl());

        TypeReference typeRefForMapper = cacheUtils.getTypeReference(method);

        if (rawReturnType.isAssignableFrom(Mono.class)) {

            return reactiveRedisTemplate
                    .opsForValue()
                    .get(CompKey)
                    .map(cacheResponse -> objectMapper.convertValue(cacheResponse, typeRefForMapper))
                    .switchIfEmpty(Mono.defer(() -> methodMonoResponseToCache(joinPoint, CompKey, time)));

        } else if (rawReturnType.isAssignableFrom(Flux.class)) {

            return reactiveRedisTemplate
                    .opsForValue()
                    .get(CompKey)
                    .flatMapMany(cacheResponse -> Flux.fromIterable(
                            (List) ((List) cacheResponse)
                                    .stream()
                                    .map(cacheData -> objectMapper.convertValue(cacheData, typeRefForMapper))
                                    .collect(Collectors.toList())))
                    .switchIfEmpty(Flux.defer(() -> methodFluxResponseToCache(joinPoint, CompKey, time)));
        }
        throw new RuntimeException("ReactiveRedisCacheable : 매핑된 메소드의 리턴 타입이 Mono 와 Flux 외에는 지원되지 않습니다.");
    }



    private Mono<?> methodMonoResponseToCache(ProceedingJoinPoint joinPoint, String key, Duration time) {
        try {
            return ((Mono<?>) joinPoint.proceed(joinPoint.getArgs()))
                    .map(methodResponse -> {
                        reactiveRedisTemplate
                                .opsForValue()
                                .set( key, methodResponse, time)
                                .subscribe();

                        return methodResponse;
                    });
        } catch (Throwable e) {
            return Mono.error(e);
        }
    }

    private Flux<?> methodFluxResponseToCache(ProceedingJoinPoint joinPoint, String key, Duration time) {
        try {
            return ((Flux<?>) joinPoint.proceed(joinPoint.getArgs()))
                    .collectList()
                    .map(methodResponseList -> {
                        reactiveRedisTemplate
                                .opsForValue()
                                .set(key, methodResponseList, time)
                                .subscribe();

                        return methodResponseList;
                    })
                    .flatMapMany(Flux::fromIterable);

        } catch (Throwable e) {
            return Flux.error(e);
        }
    }
}
