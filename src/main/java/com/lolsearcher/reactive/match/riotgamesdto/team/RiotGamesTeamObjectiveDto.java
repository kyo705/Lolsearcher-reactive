package com.lolsearcher.reactive.match.riotgamesdto.team;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RiotGamesTeamObjectiveDto implements Serializable {
    private boolean first;
    private int kills;
}
