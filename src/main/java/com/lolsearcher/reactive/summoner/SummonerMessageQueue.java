package com.lolsearcher.reactive.summoner;

import reactor.core.publisher.Mono;

public interface SummonerMessageQueue {

    Mono<Void> send(String key, SummonerDto summoner);

    Mono<Void> send(SummonerDto summoner);
}
