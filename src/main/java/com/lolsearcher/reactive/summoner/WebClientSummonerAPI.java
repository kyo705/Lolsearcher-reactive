package com.lolsearcher.reactive.summoner;

import com.lolsearcher.reactive.cache.ReactiveRedisCacheable;
import com.lolsearcher.reactive.errors.exception.IllegalRiotGamesResponseDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.summoner.SummonerConstant.*;

@RequiredArgsConstructor
@Component
public class WebClientSummonerAPI implements SummonerAPI {

    private final WebClient koreaWebClient;
    @Value("${lolsearcher.riot_api_key}") private String key;

    @ReactiveRedisCacheable(name = SUMMONER_CACHE_KEY, key = "#name", ttl = "${lolsearcher.cache.summoner.ttl}")
    @Override
    public Mono<RiotGamesSummonerDto> findByName(String name) {

        return koreaWebClient
                .get()
                .uri(RIOTGAMES_SUMMONER_WITH_NAME_URI, name, key)
                .retrieve()
                .bodyToMono(RiotGamesSummonerDto.class)
                .doOnNext(RiotGamesSummonerDto::validate);
    }

    @ReactiveRedisCacheable(name = SUMMONER_CACHE_KEY, key = "#summonerId", ttl = "${lolsearcher.cache.summoner.ttl}")
    @Override
    public Mono<RiotGamesSummonerDto> findById(String summonerId) {

        return koreaWebClient
                .get()
                .uri(RIOTGAMES_SUMMONER_WITH_ID_URI, summonerId, key)
                .retrieve()
                .bodyToMono(RiotGamesSummonerDto.class)
                .doOnNext(RiotGamesSummonerDto::validate)
                .onErrorResume(throwable -> {
                    if(throwable instanceof  IllegalArgumentException || throwable instanceof DecodingException){
                        return Mono.error(new IllegalRiotGamesResponseDataException(throwable.getMessage()));
                    }
                    return Mono.error(throwable);
                });
    }
}
