package com.lolsearcher.reactive.model.input.front;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class RequestMatchDto {

    private final String summonerId;
    private final String puuid;
    private final String lastMatchId;
    private final int matchCount;
    private final int queueId;
    private final int championId;

    public RequestMatchDto(){
        this.summonerId = "";
        this.puuid = "";
        this.lastMatchId = "";
        this.championId = -1; /* -1 : 모든 챔피언을 의미 */
        this.queueId = -1; /* -1 : 모든 매치 큐를 의미 */
        this.matchCount = 20;
    }
}
