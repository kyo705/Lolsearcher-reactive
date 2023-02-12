package com.lolsearcher.reactive.model.entity.summoner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Summoner {

    private Long id;
    private String summonerId;
    private String accountId;
    private String puuid;
    private String summonerName;
    private String lastMatchId;
    private long revisionDate;
    private int profileIconId;
    private long summonerLevel;
}
