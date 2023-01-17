package com.lolsearcher.reactive.model.input.riotgames.match.perk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RiotGamesMatchPerkStyleSelectionDto implements Serializable {
	private short perk;
	private short var1;
	private short var2;
	private short var3;
}
