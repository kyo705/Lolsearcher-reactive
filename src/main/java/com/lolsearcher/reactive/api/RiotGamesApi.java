package com.lolsearcher.reactive.api;

import com.lolsearcher.reactive.model.input.riotgames.match.RiotGamesTotalMatchDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RiotGamesApi {

    Flux<String> getMatchIds(String puuid, String lastMatchId, int count);

    Mono<RiotGamesTotalMatchDto> getMatches(String matchId);
}
