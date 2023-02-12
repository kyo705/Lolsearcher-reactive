package com.lolsearcher.reactive.controller;

import com.lolsearcher.reactive.model.input.front.RequestSummonerDto;
import com.lolsearcher.reactive.model.input.front.RequestUpdatingSummonerDto;
import com.lolsearcher.reactive.model.output.summoner.SummonerDto;
import com.lolsearcher.reactive.service.search.summoner.SummonerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/renew/summoner")
public class SummonerController {

    private final SummonerService summonerService;

    @PostMapping
    public Mono<SummonerDto> getRenewSummoner(@RequestBody @Valid RequestSummonerDto requestSummonerDto){

        return summonerService.getRenewSummoner(requestSummonerDto);
    }

    @PutMapping
    public Flux<SummonerDto> updateSummoners(@RequestBody @Valid RequestUpdatingSummonerDto request){

        return summonerService.updateSameNameSummoners(request);
    }
}
