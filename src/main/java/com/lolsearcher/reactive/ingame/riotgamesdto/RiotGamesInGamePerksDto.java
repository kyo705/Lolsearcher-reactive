package com.lolsearcher.reactive.ingame.riotgamesdto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getRuneKey;

@Getter
@Setter
public class RiotGamesInGamePerksDto {

    List<Short> perkIds; /* 1~4: mainPerkIds,  5~6: subPerkIds,  7~9: statIds  */
    Short perkStyle;
    Short perkSubStyle;

    public Mono<RiotGamesInGamePerksDto> validate(ReactiveStringRedisTemplate template) {

        return Flux.fromIterable(perkIds).concatWith( Flux.just(perkStyle, perkSubStyle))
                .flatMap(perkId -> template.opsForValue().get(getRuneKey(perkId)).switchIfEmpty(Mono.error(new IllegalArgumentException("rune id must be in permitted boundary"))))
                .collectList()
                .map(obj -> this);
    }
}
