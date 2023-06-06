package com.lolsearcher.reactive.match;

public class MatchConstant {

    public static final String RIOTGAMES_MATCHIDS_WITH_PUUID_URI = "/lol/match/v5/matches/by-puuid/{puuid}/ids?start={start}&count={count}&api_key={key}";
    public static final String RIOTGAMES_MATCH_WITH_ID_URI = "/lol/match/v5/matches/{matchId}?api_key={key}";
    public static final String FAIL_MATCH_ID_TEMPLATE = "failMatchIdTemplate";
    public static final String SUCCESS_MATCH_TEMPLATE = "successMatchTemplate";
    public static final String REMAIN_MATCH_ID_RANGE_TEMPLATE = "remainMatchIdRangeTemplate";
    public static final String LAST_MATCH_ID_TEMPLATE = "lastMatchIdTemplate";
    public static final String FAIL_MATCH_ID_TOPIC = "failMatchId";
    public static final String SUCCESS_MATCH_TOPIC = "successMatch";
    public static final String REMAIN_MATCH_ID_RANGE_TOPIC = "remainMatchIdRange";
    public static final String LAST_MATCH_ID_TOPIC = "lastMatchId";
    public static final int THE_NUMBER_OF_MEMBER = 10;         /* Match Entity에 포함될 Team 수 */
    public static final int MATCH_DEFAULT_COUNT = 20;          /* 유저 최신 전적 가져오는 갯수 */
    public static final String MATCH_CACHE_KEY = "Match";
}
