package com.lolsearcher.reactive.model.input.riotgames.ingame;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RiotGamesInGameParticipantDto {

    long championId;
    RiotGamesInGamePerksDto perks;
    long profileIconId;
    boolean bot;
    long teamId;
    String summonerName;
    String summonerId;
    long spell1Id;
    long spell2Id;
    List<RiotGamesInGameCustomizationObjectDto> gameCustomizationObjects;
}
