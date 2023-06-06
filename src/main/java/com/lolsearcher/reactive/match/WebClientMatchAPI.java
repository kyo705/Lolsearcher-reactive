package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.cache.ReactiveRedisCacheable;
import com.lolsearcher.reactive.match.riotgamesdto.RiotGamesTotalMatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.lolsearcher.reactive.match.MatchConstant.*;

@RequiredArgsConstructor
@Component
public class WebClientMatchAPI implements MatchAPI {

    private final WebClient asiaWebClient;
    @Value("${lolsearcher.riot_api_key}") private String key;

    @Override
    public Flux<String> findMatchIds(String puuid, String lastMatchId, int count) {

        return asiaWebClient
                .get()
                .uri(RIOTGAMES_MATCHIDS_WITH_PUUID_URI, puuid, 0, count, key)
                .retrieve()
                .bodyToMono(String[].class)
                .doOnNext(strings -> Arrays.sort(strings, Comparator.reverseOrder()))
                .flatMapIterable(matchIds->recentMatchIds(matchIds, lastMatchId));
    }

    @ReactiveRedisCacheable(name = MATCH_CACHE_KEY, key = "#matchId", ttl = "${lolsearcher.cache.match.ttl}")
    @Override
    public Mono<RiotGamesTotalMatchDto> findMatch(String matchId) {

        return asiaWebClient
                .get()
                .uri(RIOTGAMES_MATCH_WITH_ID_URI, matchId, key)
                .retrieve()
                .bodyToMono(RiotGamesTotalMatchDto.class);
    }

    private List<String> recentMatchIds(String[] matchIds, String lastMatchId){
        List<String> recentMatchIds = new ArrayList<>();
        for(String matchId : matchIds){
            if(matchId.equals(lastMatchId)){
                break;
            }
            recentMatchIds.add(matchId);
        }
        return recentMatchIds;
    }
}
