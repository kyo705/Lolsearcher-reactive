package com.lolsearcher.reactive.service.search.ingame;

import com.lolsearcher.reactive.api.riot.RiotGamesApi;
import com.lolsearcher.reactive.model.factory.ResponseFactory;
import com.lolsearcher.reactive.model.input.front.RequestInGameDto;
import com.lolsearcher.reactive.model.output.ingame.InGameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class InGameService {

    private final RiotGamesApi riotGamesApi;

    public Mono<InGameDto> getInGame(RequestInGameDto request) {

        return riotGamesApi.getInGame(request.getSummonerId())
                .map(ResponseFactory::getResponseInGameDtoFromRiotGamesDto);
    }
}
