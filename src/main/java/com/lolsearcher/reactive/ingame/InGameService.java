package com.lolsearcher.reactive.ingame;

import com.lolsearcher.reactive.utils.ResponseFactory;
import com.lolsearcher.reactive.ingame.dto.InGameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class InGameService {

    private final InGameAPI inGameAPI;

    public Mono<InGameDto> findById(String summonerId) {

        return inGameAPI.findById(summonerId)
                .map(ResponseFactory::getInGameDto);
    }
}
