package com.lolsearcher.reactive.model.entity.rank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Rank {

    private Long id;
    private String summonerId;
    private int seasonId;
    private String queueType;
    private String leagueId;
    private String tier; /* GOLD, SLIVER, BRONZE ... */
    private String rank; /* I, II, III ... */
    private int leaguePoints;
    private int wins;
    private int losses;
}
