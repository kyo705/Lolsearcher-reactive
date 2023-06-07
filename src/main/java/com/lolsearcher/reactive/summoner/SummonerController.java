package com.lolsearcher.reactive.summoner;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.lolsearcher.reactive.summoner.SummonerConstant.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping
public class SummonerController {

    private final SummonerService summonerService;

    @GetMapping(SUMMONER_BY_NAME_URI)
    public Mono<SummonerDto> renewByName(
            @PathVariable @NotBlank @Size(max = SUMMONER_NAME_MAX_LENGTH, min = SUMMONER_NAME_MIN_LENGTH) String name){

        return summonerService.renewByName(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(SUMMONER_BY_NAME_URI)
    public Flux<SummonerDto> updateSummoners(
            @PathVariable @NotBlank @Size(max = SUMMONER_NAME_MAX_LENGTH, min = SUMMONER_NAME_MIN_LENGTH) String name,
            @RequestBody @Valid SummonerUpdateRequest request){

        return summonerService.updateSameNameSummoners(name, request);
    }
}
