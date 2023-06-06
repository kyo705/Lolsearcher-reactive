package com.lolsearcher.reactive.rank;

import reactor.core.publisher.Mono;

public interface RankMessageQueue {

    Mono<Void> send(String key, RankDto rank);

    Mono<Void> send(RankDto rank);
}
