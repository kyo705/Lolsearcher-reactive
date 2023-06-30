package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.match.dto.MatchDto;

public interface MatchMessageQueue {

    void sendFailMatchId(String failMatchId);
    void sendFailMatchId(String key, String failMatchId);
    void sendSuccessMatch(MatchDto successMatch);
    void sendSuccessMatch(String key, MatchDto successMatch);
    void sendRemainMatchIds(String key, RemainMatchIdRange remainMatchIdRange);
    void sendLastMatchId(String key, String lastMatchId);
}
