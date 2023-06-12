package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.match.dto.MatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.lolsearcher.reactive.match.MatchConstant.MATCH_URI;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getChampionKey;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getQueueKey;
import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@Validated
@RequiredArgsConstructor
@RestController
public class MatchController {

    private final ReactiveRedisTemplate<String, Object> template;
    private final MatchService matchService;

    @GetMapping(value = MATCH_URI, produces = {APPLICATION_NDJSON_VALUE})
    public Flux<MatchDto> findRecentMatches(@ModelAttribute @Valid MatchRequest request){

        return validateParam(request).flatMap(obj -> matchService.findRecentMatches(request));
    }

    private Flux<Object> validateParam(MatchRequest request) {

        return Flux.just("DUMMY")
                .flatMap(obj -> {
                    if(request.getChampionId() == null) {
                        return Mono.just(obj);
                    }
                    return template.opsForValue().get(getChampionKey(request.getChampionId()));
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("championId must be in permitted boundary")))
                .flatMap(obj -> {
                    if(request.getQueueId() == null) {
                        return Mono.just(obj);
                    }
                    return template.opsForValue().get(getQueueKey(request.getQueueId()));
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("queueId must be in permitted boundary")));
    }
}
