package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.match.riotgamesdto.RiotGamesTotalMatchDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MatchAPI {

    Flux<String> findMatchIds(String puuid, String lastMatchId, int count);
    Mono<RiotGamesTotalMatchDto> findMatch(String matchId);
}
