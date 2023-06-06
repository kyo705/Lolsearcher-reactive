package com.lolsearcher.reactive.match.riotgamesdto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RiotGamesMatchMetadataDto implements Serializable {
    private String dataVersion;
    private String matchId;
    private List<String> participants;   // puuid list
}
