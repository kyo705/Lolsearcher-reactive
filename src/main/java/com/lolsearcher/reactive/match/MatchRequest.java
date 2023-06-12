package com.lolsearcher.reactive.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import static com.lolsearcher.reactive.summoner.SummonerConstant.*;

@Builder
@AllArgsConstructor
@Data
public class MatchRequest {

    @NotBlank
    @Size(max = SUMMONER_ID_MAX_LENGTH, min = SUMMONER_ID_MIN_LENGTH)
    private String summonerId;
    @NotBlank
    @Size(max = PUUID_MAX_LENGTH, min = PUUID_MIN_LENGTH)
    private String puuid;

    private String lastMatchId;
    @Positive
    private int count;
    private Integer queueId;
    private Integer championId;

    public MatchRequest(){
        this.summonerId = "";
        this.puuid = "";
        this.lastMatchId = "";
        this.count = 20;
    }
}
