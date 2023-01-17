package com.lolsearcher.reactive.service;

import com.lolsearcher.reactive.constant.GameType;
import com.lolsearcher.reactive.constant.LolSearcherConstants;
import com.lolsearcher.reactive.model.input.riotgames.match.RiotGamesTotalMatchDto;

public class MatchServiceTestSetUp {

    protected static RiotGamesTotalMatchDto getApiMatchDto(String matchId){

        RiotGamesTotalMatchDto totalMatchDto = new RiotGamesTotalMatchDto();
        totalMatchDto.getMetadata().setMatchId(matchId);
        totalMatchDto.getMetadata().setDataVersion(LolSearcherConstants.MATCH_DATA_VERSION);

        int num = Character.getNumericValue(matchId.charAt(matchId.length()-1));
        if(num == 1){
            totalMatchDto.getInfo().setQueueId(GameType.SOLO_RANK_MODE.getQueueId());
        }else if(num == 2){
            totalMatchDto.getInfo().setQueueId(GameType.FLEX_RANK_MODE.getQueueId());
        }else if(num == 3){
            totalMatchDto.getInfo().setQueueId(GameType.NORMAL_MODE.getQueueId());
        }

        return totalMatchDto;
    }
}
