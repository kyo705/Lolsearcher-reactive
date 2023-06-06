package com.lolsearcher.reactive.ingame;

import com.lolsearcher.reactive.ingame.riotgamesdto.RiotGamesInGameDto;
import reactor.core.publisher.Mono;

public interface InGameAPI {

    Mono<RiotGamesInGameDto>  findById(String summonerId);
}
