package com.lolsearcher.reactive.service.ban;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.lolsearcher.reactive.constant.LolSearcherConstants.SEARCH_BAN_COUNT;

@Slf4j
@RequiredArgsConstructor
@Service
public class BanService {

    @Value("${lolsearcher.cache.abuser.ttl}")
    private Integer ttl;

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public Mono<String> inspect(ServerWebExchange exchange) {

        if(exchange.getRequest().getHeaders().get("X-Forwarded-For") == null){
            return Mono.just("invalid");
        }

        String ipAddress = exchange.getRequest().getHeaders().get("X-Forwarded-For").get(0);

        return reactiveRedisTemplate.opsForValue().get(ipAddress).flatMap(count->{
            if(count==null || (Integer)count< SEARCH_BAN_COUNT){
                return Mono.empty();
            }else{
                return Mono.just("invalid");
            }
        });
    }

    public Mono<Integer> addAbusingCount(String ipAddress) {

        return reactiveRedisTemplate.opsForValue().get(ipAddress)
                .flatMap(count->
                        reactiveRedisTemplate.opsForValue().set(ipAddress, (Integer)count+1, Duration.ofDays(ttl))
                                .flatMap(o->Mono.just((Integer)count+1))
                )
                .switchIfEmpty(Mono.defer(() ->
                        reactiveRedisTemplate.opsForValue().set(ipAddress, 1, Duration.ofDays(ttl))
                                .flatMap(o->Mono.just(1))
                ));
    }
}
