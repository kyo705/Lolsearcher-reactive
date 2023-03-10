package com.lolsearcher.reactive.api;

import com.lolsearcher.reactive.annotation.ReactiveRedisCacheable;
import com.lolsearcher.reactive.model.input.riotgames.ingame.RiotGamesInGameDto;
import com.lolsearcher.reactive.model.input.riotgames.match.RiotGamesTotalMatchDto;
import com.lolsearcher.reactive.model.input.riotgames.rank.RiotGamesRankDto;
import com.lolsearcher.reactive.model.input.riotgames.summoner.RiotGamesSummonerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import static com.lolsearcher.reactive.constant.constant.BeanNameConstants.ASIA_WEB_CLIENT_NAME;
import static com.lolsearcher.reactive.constant.constant.BeanNameConstants.KR_WEB_CLIENT_NAME;
import static com.lolsearcher.reactive.constant.constant.CacheConstant.*;
import static com.lolsearcher.reactive.constant.constant.UriConstants.*;

@RequiredArgsConstructor
@Component
public class RiotGamesApiVer2 implements RiotGamesApi {

    @Value("${riot_api_key}")
    private String key;

    private final Map<String, WebClient> webclients;

    @ReactiveRedisCacheable(name = SUMMONER_KEY, key = "#name", ttl = "${lolsearcher.cache.summoner.ttl}")
    @Override
    public Mono<RiotGamesSummonerDto> getSummonerByName(String name) {

        return webclients.get(KR_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_SUMMONER_WITH_NAME_URI, name, key)
                .retrieve()
                .bodyToMono(RiotGamesSummonerDto.class);
    }

    @ReactiveRedisCacheable(name = SUMMONER_KEY, key = "#summonerId", ttl = "${lolsearcher.cache.summoner.ttl}")
    @Override
    public Mono<RiotGamesSummonerDto> getSummonerById(String summonerId) {

        return webclients.get(KR_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_SUMMONER_WITH_ID_URI, summonerId, key)
                .retrieve()
                .bodyToMono(RiotGamesSummonerDto.class);
    }

    @ReactiveRedisCacheable(name = RANK_KEY, key = "#summonerId", ttl = "${lolsearcher.cache.rank.ttl}")
    @Override
    public Flux<RiotGamesRankDto> getRank(String summonerId) {

        return webclients.get(KR_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_RANK_WITH_ID_URI, summonerId, key)
                .retrieve()
                .bodyToFlux(RiotGamesRankDto.class);
    }

    @Override
    public Flux<String> getMatchIds(String puuid, String lastMatchId, int count) {

        return webclients.get(ASIA_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_MATCHIDS_WITH_PUUID_URI, puuid, 0, count, key)
                .retrieve()
                .bodyToMono(String[].class)
                .doOnNext(strings -> Arrays.sort(strings, Comparator.reverseOrder()))
                .flatMapIterable(matchIds->recentMatchIds(matchIds, lastMatchId));
    }

    @ReactiveRedisCacheable(name = MATCH_KEY, key = "#matchId", ttl = "${lolsearcher.cache.match.ttl}")
    @Override
    public Mono<RiotGamesTotalMatchDto> getMatches(String matchId) {

        return webclients.get(ASIA_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_MATCH_WITH_ID_URI, matchId, key)
                .retrieve()
                .bodyToMono(RiotGamesTotalMatchDto.class);
    }

    @ReactiveRedisCacheable(name = IN_GAME_KEY, key = "#summonerId", ttl = "${lolsearcher.cache.ingame.ttl}")
    @Override
    public Mono<RiotGamesInGameDto> getInGame(String summonerId) {

        return webclients.get(KR_WEB_CLIENT_NAME)
                .get()
                .uri(RIOTGAMES_INGAME_WITH_ID_URI, summonerId, key)
                .retrieve()
                .bodyToMono(RiotGamesInGameDto.class);
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
