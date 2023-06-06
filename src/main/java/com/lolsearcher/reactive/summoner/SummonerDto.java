package com.lolsearcher.reactive.summoner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class SummonerDto {

    private String accountId;
    private int profileIconId;
    private String summonerName;
    private String summonerId;
    private String puuid;
    private long summonerLevel;
}
