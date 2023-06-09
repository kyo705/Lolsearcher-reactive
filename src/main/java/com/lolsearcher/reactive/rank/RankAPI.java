package com.lolsearcher.reactive.rank;

import reactor.core.publisher.Flux;

public interface RankAPI {

    Flux<RiotGamesRankDto> findAll(String summonerId);
}
