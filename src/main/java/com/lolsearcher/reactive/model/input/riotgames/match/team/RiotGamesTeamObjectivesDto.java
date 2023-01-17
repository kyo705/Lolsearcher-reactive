package com.lolsearcher.reactive.model.input.riotgames.match.team;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RiotGamesTeamObjectivesDto implements Serializable {
    private RiotGamesTeamObjectiveDto baron;
    private RiotGamesTeamObjectiveDto champion;
    private RiotGamesTeamObjectiveDto dragon;
    private RiotGamesTeamObjectiveDto inhibitor;
    private RiotGamesTeamObjectiveDto riftHerald;
    private RiotGamesTeamObjectiveDto tower;

}
