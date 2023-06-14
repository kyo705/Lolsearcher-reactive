package com.lolsearcher.reactive.match.riotgamesdto.perk;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getRuneKey;

@Getter
@Setter
public class RiotGamesMatchPerkStatsDto {
	private Short defense;
	private Short flex;
	private Short offense;

	public Mono<RiotGamesMatchPerkStatsDto> validate(ReactiveStringRedisTemplate template) {

		return Mono.just("DUMMY")
				.flatMap(obj -> template.opsForValue().get(getRuneKey(defense))
						.switchIfEmpty(Mono.error(new IllegalArgumentException("runeId must be in permitted boundary"))))
				.flatMap(obj -> template.opsForValue().get(getRuneKey(flex))
						.switchIfEmpty(Mono.error(new IllegalArgumentException("runeId must be in permitted boundary"))))
				.flatMap(obj -> template.opsForValue().get(getRuneKey(offense))
						.switchIfEmpty(Mono.error(new IllegalArgumentException("runeId must be in permitted boundary"))))
				.map(obj -> this);
	}
}
