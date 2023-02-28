package com.lolsearcher.reactive.model.input.kafka;

import com.lolsearcher.reactive.model.entity.match.Match;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SummonerMatchData {

    private String puuId;
    private String summonerId;
    private String lastMatchId;
    private List<Match> successMatches;
    private List<String> failMatchIds;
    private RemainMatchIdRange remainMatchIdRange;
}
