package com.lolsearcher.reactive.api;

import com.lolsearcher.reactive.model.input.riotgames.ingame.RiotGamesInGameDto;
import com.lolsearcher.reactive.model.input.riotgames.match.RiotGamesTotalMatchDto;
import com.lolsearcher.reactive.model.input.riotgames.rank.RiotRankDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RiotGamesApi {

    Flux<String> getMatchIds(String puuid, String lastMatchId, int count);

    Mono<RiotGamesTotalMatchDto> getMatches(String matchId);

    Mono<RiotGamesInGameDto> getInGame(String summonerId);

    Flux<RiotRankDto> getRank(String summonerId);
}
