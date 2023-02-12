package com.lolsearcher.reactive.controller;

import com.lolsearcher.reactive.model.input.front.RequestMatchDto;
import com.lolsearcher.reactive.model.output.match.MatchDto;
import com.lolsearcher.reactive.service.search.match.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MatchController {

    private final MatchService matchService;

    @PostMapping(value = "/renew/summoner/match", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MatchDto> getRecentMatchDto(@RequestBody @Valid RequestMatchDto request){

        return matchService.getRecentMatchDto(request);
    }
}
