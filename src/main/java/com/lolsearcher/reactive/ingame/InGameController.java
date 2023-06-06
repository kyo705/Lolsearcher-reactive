package com.lolsearcher.reactive.ingame;

import com.lolsearcher.reactive.ingame.dto.InGameDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.lolsearcher.reactive.ingame.InGameConstant.INGAME_URI;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_ID_MAX_LENGTH;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_ID_MIN_LENGTH;

@Slf4j
@RequiredArgsConstructor
@RestController
public class InGameController {

    private final InGameService inGameService;

    @GetMapping(path = INGAME_URI)
    public Mono<InGameDto> findById(
            @PathVariable @NotBlank @Size(max = SUMMONER_ID_MAX_LENGTH, min = SUMMONER_ID_MIN_LENGTH) String summonerId
    ) {

        return inGameService.findById(summonerId);
    }
}
