package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.match.dto.MatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MatchController {

    private final MatchService matchService;

    @GetMapping(value = "/renew/summoner/{summonerId}/match", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MatchDto> findRecentMatches(@ModelAttribute @Valid MatchRequest request){

        return matchService.findRecentMatches(request);
    }
}
