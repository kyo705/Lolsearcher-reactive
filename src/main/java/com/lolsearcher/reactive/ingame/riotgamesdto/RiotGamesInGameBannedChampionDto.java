package com.lolsearcher.reactive.ingame.riotgamesdto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import static com.google.common.base.Preconditions.checkArgument;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getChampionKey;

@Getter
@Setter
public class RiotGamesInGameBannedChampionDto {

    int pickTurn;
    long championId;
    long teamId;

    public Mono<RiotGamesInGameBannedChampionDto> validate(ReactiveStringRedisTemplate template) {

        return Mono.just(" ")
                .doOnNext(empty -> checkArgument(pickTurn >= 1 && pickTurn <= 10, "pickTurn must be between 1 and 10"))
                .doOnNext(empty -> checkArgument(teamId == 100 /* red */ || teamId == 200 /* blue */, "teamId must be 100 or 200"))
                .flatMap(empty -> template.opsForValue().get(getChampionKey(championId))
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("championId must be in permitted boundary"))))
                .map(obj -> this);
    }
}
