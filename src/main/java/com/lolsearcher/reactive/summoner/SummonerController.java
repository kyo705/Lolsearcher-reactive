package com.lolsearcher.reactive.summoner;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_NAME_MAX_LENGTH;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_NAME_MIN_LENGTH;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class SummonerController {

    private final SummonerService summonerService;

    @GetMapping("/renew/summoner/{name}")
    public Mono<SummonerDto> renewByName(
            @PathVariable @Size(max = SUMMONER_NAME_MAX_LENGTH, min = SUMMONER_NAME_MIN_LENGTH) String name
    ){

        return summonerService.renewByName(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/renew/summoners")
    public Flux<SummonerDto> updateSummoners(@RequestBody @Valid SummonerUpdateRequest request){

        return summonerService.updateSameNameSummoners(request);
    }
}
