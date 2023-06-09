package com.lolsearcher.reactive.rank;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;

import static com.lolsearcher.reactive.rank.RankConstant.RANK_URI;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_ID_MAX_LENGTH;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_ID_MIN_LENGTH;

@Validated
@RequiredArgsConstructor
@RestController
public class RankController {

    private final RankService rankService;

    @GetMapping(RANK_URI)
    public Mono<Map<RankTypeState, RankDto>> findAll(
            @PathVariable @NotBlank @Size(max = SUMMONER_ID_MAX_LENGTH, min = SUMMONER_ID_MIN_LENGTH) String summonerId
    ){

        return rankService.findAll(summonerId);
    }
}
