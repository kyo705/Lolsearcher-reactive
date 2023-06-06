package com.lolsearcher.reactive.match.riotgamesdto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RiotGamesTotalMatchDto implements Serializable {
    private RiotGamesMatchMetadataDto metadata = new RiotGamesMatchMetadataDto();
    private RiotGamesMatchDto info = new RiotGamesMatchDto();
}
