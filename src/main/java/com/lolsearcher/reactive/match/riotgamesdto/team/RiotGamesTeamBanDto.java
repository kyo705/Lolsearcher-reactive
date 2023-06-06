package com.lolsearcher.reactive.match.riotgamesdto.team;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RiotGamesTeamBanDto implements Serializable {
    private int championId;
    private int pickTurn;
}
