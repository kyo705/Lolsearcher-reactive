package com.lolsearcher.reactive.api.riot;

import com.lolsearcher.reactive.model.input.riotgames.ingame.RiotGamesInGameDto;
import com.lolsearcher.reactive.model.input.riotgames.match.RiotGamesTotalMatchDto;
import com.lolsearcher.reactive.model.input.riotgames.rank.RiotGamesRankDto;
import com.lolsearcher.reactive.model.input.riotgames.summoner.RiotGamesSummonerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RiotGamesApi {

    Mono<RiotGamesSummonerDto> getSummonerByName(String name);

    Mono<RiotGamesSummonerDto> getSummonerById(String summonerId);

    Flux<RiotGamesRankDto> getRank(String summonerId);

    Flux<String> getMatchIds(String puuid, String lastMatchId, int count);

    Mono<RiotGamesTotalMatchDto> getMatches(String matchId);

    Mono<RiotGamesInGameDto> getInGame(String summonerId);
}
