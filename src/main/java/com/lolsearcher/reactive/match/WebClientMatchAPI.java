package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.cache.ReactiveRedisCacheable;
import com.lolsearcher.reactive.errors.exception.IllegalRiotGamesResponseDataException;
import com.lolsearcher.reactive.match.riotgamesdto.RiotGamesTotalMatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.codec.DecodingException;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    private final ReactiveStringRedisTemplate template;
    private final WebClient asiaWebClient;
    @Value("${lolsearcher.riot_api_key}") private String key;

    @Override
    public Flux<String> findMatchIds(String puuid, String lastMatchId, int count, Integer queueId) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add(PUUID_URI_PARAM_START, Integer.toString(0));
        params.add(PUUID_URI_PARAM_COUNT, Integer.toString(count));
        params.add(RIOT_GAMES_PARAM_KEY, key);
        if(queueId != null){
            params.add(PUUID_URI_PARAM_QUEUE, Integer.toString(queueId));
        }

        return asiaWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(RIOTGAMES_MATCHIDS_WITH_PUUID_URI).queryParams(params).build(puuid))
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
                .bodyToMono(RiotGamesTotalMatchDto.class)
                .flatMap(dto -> dto.validate(template))
                .onErrorResume(throwable -> {
                    if(throwable instanceof  IllegalArgumentException || throwable instanceof DecodingException){
                        return Mono.error(new IllegalRiotGamesResponseDataException(throwable.getMessage()));
                    }
                    return Mono.error(throwable);
                });
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
