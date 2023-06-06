package com.lolsearcher.reactive.rank;

import com.lolsearcher.reactive.cache.ReactiveRedisCacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static com.lolsearcher.reactive.rank.RankConstant.RANK_CACHE_KEY;
import static com.lolsearcher.reactive.rank.RankConstant.RIOT_GAMES_RANK_WITH_ID_URI;

@RequiredArgsConstructor
@Component
public class WebClientRankAPI implements RankAPI {

    private final WebClient koreaWebClient;
    @Value("${lolsearcher.riot_api_key}") private String key;

    @ReactiveRedisCacheable(name = RANK_CACHE_KEY, key = "#summonerId", ttl = "${lolsearcher.cache.rank.ttl}")
    @Override
    public Flux<RiotGamesRankDto> findAllBySummonerId(String summonerId) {

        return koreaWebClient.get()
                .uri(RIOT_GAMES_RANK_WITH_ID_URI, summonerId, key)
                .retrieve()
                .bodyToFlux(RiotGamesRankDto.class)
                .doOnNext(RiotGamesRankDto::validate);
    }
}
