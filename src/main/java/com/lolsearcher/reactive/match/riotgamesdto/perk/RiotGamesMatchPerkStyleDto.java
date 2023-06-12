package com.lolsearcher.reactive.match.riotgamesdto.perk;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getRuneKey;

@Getter
@Setter
public class RiotGamesMatchPerkStyleDto implements Serializable {
	private String description;
	private List<RiotGamesMatchPerkStyleSelectionDto> selections;
	private short style;

	public Mono<RiotGamesMatchPerkStyleDto> validate(ReactiveRedisTemplate<String, Object> template) {

		return Flux.fromIterable(selections)
				.flatMap(selection -> template.opsForValue().get(getRuneKey(selection.getPerk()))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("runeId must be in permitted boundary"))))
				.collectList()
				.map(obj -> this);
	}
}
