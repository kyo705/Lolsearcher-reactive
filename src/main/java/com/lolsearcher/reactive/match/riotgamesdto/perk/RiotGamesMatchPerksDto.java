package com.lolsearcher.reactive.match.riotgamesdto.perk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RiotGamesMatchPerksDto implements Serializable {
	private RiotGamesMatchPerkStatsDto statPerks;
	private List<RiotGamesMatchPerkStyleDto> styles;

}
