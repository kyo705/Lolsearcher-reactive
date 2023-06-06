package com.lolsearcher.reactive.rank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class RankDto {

    private String summonerId;
    private int seasonId;
    private RankTypeState queueType;
    private String leagueId;
    private TierState tier;
    private RankState rank;
    private int leaguePoints;
    private long wins;
    private long losses;
}
