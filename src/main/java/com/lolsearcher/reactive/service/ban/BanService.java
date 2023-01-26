package com.lolsearcher.reactive.service.ban;

import com.lolsearcher.reactive.constant.LolSearcherConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class BanService {

    @Value("${lolsearcher.cache.abuser.ttl}")
    private Integer ttl;

    private final ReactiveRedisTemplate<String, Object> abusingCountTemplate;

    public Mono<String> inspect(String ipAddress) {

        return abusingCountTemplate.opsForValue().get(ipAddress).flatMap(count->{
            if(count==null || (Integer)count< LolSearcherConstants.SEARCH_BAN_COUNT){
                return Mono.empty();
            }else{
                return Mono.just("invalid");
            }
        });
    }

    public Mono<Integer> addAbusingCount(String ipAddress) {

        return abusingCountTemplate.opsForValue().get(ipAddress)
                .flatMap(count->
                        abusingCountTemplate.opsForValue().set(ipAddress, (Integer)count+1, Duration.ofDays(ttl))
                                .flatMap(o->Mono.just((Integer)count+1))
                )
                .switchIfEmpty(Mono.defer(() ->
                        abusingCountTemplate.opsForValue().set(ipAddress, 1, Duration.ofDays(ttl))
                                .flatMap(o->Mono.just(1))
                ));
    }
}
