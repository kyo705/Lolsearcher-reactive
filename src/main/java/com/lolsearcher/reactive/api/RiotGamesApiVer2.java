package com.lolsearcher.reactive.api;

import com.lolsearcher.reactive.annotation.ReactiveRedisCacheable;
import com.lolsearcher.reactive.model.input.riotgames.ingame.RiotGamesInGameDto;
import com.lolsearcher.reactive.model.input.riotgames.match.RiotGamesTotalMatchDto;
import com.lolsearcher.reactive.model.input.riotgames.rank.RiotRankDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lolsearcher.reactive.constant.BeanNameConstants.ASIA_WEB_CLIENT_NAME;
import static com.lolsearcher.reactive.constant.BeanNameConstants.KR_WEB_CLIENT_NAME;
import static com.lolsearcher.reactive.constant.UriConstants.*;

@RequiredArgsConstructor
@Component
public class RiotGamesApiVer2 implements RiotGamesApi {

    @Value("${riot_api_key}")
    private String key;

    private final Map<String, WebClient> webclients;

    @Override
    public Flux<String> getMatchIds(String puuid, String lastMatchId, int count) {
        return webclients.get(ASIA_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_MATCHIDS_WITH_PUUID_URI, puuid, 0, count, key)
                .retrieve()
                .bodyToMono(String[].class)
                .flatMapIterable(matchIds->recentMatchIds(matchIds, lastMatchId));
    }

    @ReactiveRedisCacheable(key = "#matchId", ttl = "${lolsearcher.cache.match.ttl}")
    @Override
    public Mono<RiotGamesTotalMatchDto> getMatches(String matchId) {

        return webclients.get(ASIA_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_MATCH_WITH_ID_URI, matchId, key)
                .retrieve()
                .bodyToMono(RiotGamesTotalMatchDto.class);
    }

    @ReactiveRedisCacheable(key = "#summonerId", ttl = "${lolsearcher.cache.ingame.ttl}")
    @Override
    public Mono<RiotGamesInGameDto> getInGame(String summonerId) {

        return webclients.get(KR_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_INGAME_WITH_ID_URI, summonerId, key)
                .retrieve()
                .bodyToMono(RiotGamesInGameDto.class);
    }

    @Override
    public Flux<RiotRankDto> getRank(String summonerId) {

        return webclients.get(KR_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_RANK_WITH_ID_URI, summonerId, key)
                .retrieve()
                .bodyToFlux(RiotRankDto.class);
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
