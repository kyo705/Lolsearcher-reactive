package com.lolsearcher.reactive.model.input.riotgames.match.perk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RiotGamesMatchPerkStatsDto implements Serializable {
	private Short defense;
	private Short flex;
	private Short offense;
}
