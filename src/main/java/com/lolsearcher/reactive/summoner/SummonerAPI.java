package com.lolsearcher.reactive.summoner;

import reactor.core.publisher.Mono;

public interface SummonerAPI {

    Mono<RiotGamesSummonerDto> findByName(String name);
    Mono<RiotGamesSummonerDto> findById(String id);
}
