package com.lolsearcher.reactive.model.input.riotgames.summoner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiotGamesSummonerDto {

    private String accountId;
    private int profileIconId;
    private long revisionDate;
    private String name;
    private String id;
    private String puuid;
    private long summonerLevel;
}
