package com.lolsearcher.reactive.match.riotgamesdto.perk;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RiotGamesMatchPerksDto implements Serializable {
	private RiotGamesMatchPerkStatsDto statPerks;
	private List<RiotGamesMatchPerkStyleDto> styles;

    public Mono<RiotGamesMatchPerksDto> validate(ReactiveRedisTemplate<String, Object> template) {

        return Mono.just("DUMMY")
                .flatMap(obj -> statPerks.validate(template))
                .flatMapIterable(obj -> styles).flatMap(style -> style.validate(template)).collectList()
                .map(obj -> this);
    }
}
