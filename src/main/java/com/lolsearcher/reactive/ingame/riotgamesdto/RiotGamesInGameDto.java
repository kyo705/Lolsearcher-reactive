package com.lolsearcher.reactive.ingame.riotgamesdto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RiotGamesInGameDto {

    long gameId;
    String gameType;
    long gameStartTime;
    long mapId;
    long gameLength;
    String platformId;
    String gameMode;
    List<RiotGamesInGameBannedChampionDto> bannedChampions;
    long gameQueueConfigId;
    List<RiotGamesInGameParticipantDto> participants;

    public void validate() {
    }
}
