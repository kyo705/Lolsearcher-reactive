package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.match.dto.MatchDto;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TotalMatchRecordDto {

    private String puuId;
    private String summonerId;
    private String lastMatchId;
    private List<MatchDto> successMatches;
    private List<String> failMatchIds;
    private RemainMatchIdRange remainMatchIdRange;

}

