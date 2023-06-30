package com.lolsearcher.reactive.match;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TotalMatchRecordDto {

    private String summonerId;
    private String lastMatchId;
    private String puuId;
    private RemainMatchIdRange remainMatchIdRange;

}

