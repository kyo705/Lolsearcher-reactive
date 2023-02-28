package com.lolsearcher.reactive.unit.service.kafka.facade;

import com.lolsearcher.reactive.model.entity.match.Match;
import com.lolsearcher.reactive.model.input.kafka.RemainMatchIdRange;
import com.lolsearcher.reactive.model.input.kafka.SummonerMatchData;

import java.util.ArrayList;
import java.util.List;

public class FacadeServiceTestSetup {


    public static SummonerMatchData getSummonerMatchData() {

        List<Match> successMatches = new ArrayList<>();
        successMatches.add(new Match());
        List<String> failMatchIds = new ArrayList<>();
        failMatchIds.add("matchId3");
        RemainMatchIdRange remainMatchIdRange = new RemainMatchIdRange();
        remainMatchIdRange.setStartRemainMatchId("matchId2");
        remainMatchIdRange.setEndRemainMatchId("matchId1");

        SummonerMatchData summonerMatchData = new SummonerMatchData();
        summonerMatchData.setPuuId("puuid");
        summonerMatchData.setSummonerId("summonerId");
        summonerMatchData.setLastMatchId("matchId4");
        summonerMatchData.setSuccessMatches(successMatches);
        summonerMatchData.setFailMatchIds(failMatchIds);
        summonerMatchData.setRemainMatchIdRange(remainMatchIdRange);

        return summonerMatchData;
    }
}
