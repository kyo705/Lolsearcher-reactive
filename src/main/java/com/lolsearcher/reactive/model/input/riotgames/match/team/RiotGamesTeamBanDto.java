package com.lolsearcher.reactive.model.input.riotgames.match.team;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RiotGamesTeamBanDto implements Serializable {
    private int championId;
    private int pickTurn;
}
