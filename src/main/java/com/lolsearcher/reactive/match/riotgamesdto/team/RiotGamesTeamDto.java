package com.lolsearcher.reactive.match.riotgamesdto.team;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RiotGamesTeamDto implements Serializable {
    private List<RiotGamesTeamBanDto> bans;
    private RiotGamesTeamObjectivesDto objectives;
    private short teamId;
    private boolean win;
}
