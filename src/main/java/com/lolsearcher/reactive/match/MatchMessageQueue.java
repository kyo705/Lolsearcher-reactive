package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.match.dto.MatchDto;
import reactor.core.publisher.Mono;

public interface MatchMessageQueue {

    Mono<Void> sendFailMatchId(String failMatchId);
    Mono<Void> sendFailMatchId(String key, String failMatchId);
    Mono<Void> sendSuccessMatch(MatchDto successMatch);
    Mono<Void> sendSuccessMatch(String key, MatchDto successMatch);
    Mono<Void> sendRemainMatchIds(String key, RemainMatchIdRange remainMatchIdRange);
    Mono<Void> sendLastMatchId(String key, String lastMatchId);
}
