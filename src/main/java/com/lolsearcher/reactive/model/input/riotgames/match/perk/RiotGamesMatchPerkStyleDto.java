package com.lolsearcher.reactive.model.input.riotgames.match.perk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RiotGamesMatchPerkStyleDto implements Serializable {
	private String description;
	private List<RiotGamesMatchPerkStyleSelectionDto> selections;
	private short style;
}
