package com.lolsearcher.reactive.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class MatchRequest {

    private String summonerId;
    private String puuid;
    private String lastMatchId;
    private int matchCount;
    private int queueId;
    private int championId;

    public MatchRequest(){
        this.summonerId = "";
        this.puuid = "";
        this.lastMatchId = "";
        this.championId = -1;  /* -1 : 모든 챔피언을 의미 */
        this.queueId = -1;     /* -1 : 모든 매치 큐를 의미 */
        this.matchCount = 20;
    }
}
