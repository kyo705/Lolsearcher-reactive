package com.lolsearcher.reactive.controller;

import com.lolsearcher.reactive.model.input.front.RequestInGameDto;
import com.lolsearcher.reactive.model.output.ingame.InGameDto;
import com.lolsearcher.reactive.service.ingame.InGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class InGameController {

    private final InGameService inGameService;

    @PostMapping(path = "/summoner/ingame")
    public Mono<InGameDto> getInGame(@RequestBody @Valid RequestInGameDto request){
        return inGameService.getInGame(request);
    }
}
