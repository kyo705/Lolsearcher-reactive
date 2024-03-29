package com.lolsearcher.reactive.match.riotgamesdto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class RiotGamesTotalMatchDto {
    private RiotGamesMatchMetadataDto metadata = new RiotGamesMatchMetadataDto();
    private RiotGamesMatchDto info = new RiotGamesMatchDto();

    public Mono<RiotGamesTotalMatchDto> validate(ReactiveStringRedisTemplate template) {

        return Mono.just("DUMMY")
                .flatMap(obj -> metadata.validate())
                .flatMap(obj -> info.validate(template))
                .map(obj -> this);
    }
}
