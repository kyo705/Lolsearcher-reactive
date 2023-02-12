package com.lolsearcher.reactive.model.output.summoner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class SummonerDto {

    private String name;
    private int profileIconId;
    private long summonerLevel;
    private String summonerId;
    private String puuId;
}
