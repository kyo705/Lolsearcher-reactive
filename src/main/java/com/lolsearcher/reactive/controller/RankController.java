package com.lolsearcher.reactive.controller;

import com.lolsearcher.reactive.model.input.front.RequestRankDto;
import com.lolsearcher.reactive.model.output.rank.RankDto;
import com.lolsearcher.reactive.service.rank.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class RankController {

    private final RankService rankService;

    @PostMapping("/summoner/rank/renew")
    public Mono<Map<String, RankDto>> getRankDto(@RequestBody @Valid RequestRankDto requestRankDto){

        return rankService.getRankDto(requestRankDto);
    }
}
