package com.lolsearcher.reactive.ban;

import com.lolsearcher.reactive.errors.exception.NonAuthorizedSearchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.lolsearcher.reactive.ban.BanConstant.SEARCH_BAN_COUNT;

@Slf4j
@RequiredArgsConstructor
@Service
public class BanService {

    @Value("${lolsearcher.cache.abuser.ttl}")
    private Integer ttl;

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public Mono<Void> inspect(ServerWebExchange exchange) {

        if(exchange.getRequest().getRemoteAddress() == null) {
            log.error("클라이언트 IP 주소가 없음");
            return Mono.error(new NonAuthorizedSearchException(null));
        }
        String ipAddress = exchange.getRequest().getRemoteAddress().getHostString();

        return reactiveRedisTemplate.opsForValue()
                .get(ipAddress)
                .flatMap(count->{
                    if((Integer)count < SEARCH_BAN_COUNT){
                        return Mono.empty();
                    }else{
                        log.info("IP : {} 는 접근 권한이 없음", ipAddress);
                        return Mono.error(new NonAuthorizedSearchException(ipAddress));
                    }
                });
    }

    public Mono<Long> addAbusingCount(String ipAddress) {

        return reactiveRedisTemplate.opsForValue()
                .get(ipAddress)
                .flatMap(count-> reactiveRedisTemplate.opsForValue().increment(ipAddress))
                .switchIfEmpty(Mono.defer(() ->
                        reactiveRedisTemplate.opsForValue().set(ipAddress, 1, Duration.ofDays(ttl))
                                .flatMap(o->Mono.just(1L))
                ));
    }
}
