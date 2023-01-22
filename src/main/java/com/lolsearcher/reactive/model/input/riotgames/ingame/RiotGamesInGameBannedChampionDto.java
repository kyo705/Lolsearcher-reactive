package com.lolsearcher.reactive.model.input.riotgames.ingame;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiotGamesInGameBannedChampionDto {

    int pickTurn;
    long championId;
    long teamId;
}
