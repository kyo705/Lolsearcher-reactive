package com.lolsearcher.reactive.constant.constant;

public class KafkaConstant {

    public static final String BOOTSTRAP_SERVER = "localhost:9092";


    /* SPRING REACTIVE KAFKA TEMPLATE BEAN NAME */

    public static final String SUMMONER_TEMPLATE = "summonerTemplate";
    public static final String RANK_TEMPLATE = "rankTemplate";
    public static final String FAIL_MATCH_ID_TEMPLATE = "failMatchIdTemplate";
    public static final String SUCCESS_MATCH_TEMPLATE = "successMatchTemplate";
    public static final String REMAIN_MATCH_ID_RANGE_TEMPLATE = "remainMatchIdRangeTemplate";
    public static final String LAST_MATCH_ID_TEMPLATE = "lastMatchIdTemplate";


    /* KAFKA TOPIC NAME */

    public static final String SUMMONER_TOPIC = "summoner";
    public static final String RANK_TOPIC = "rank";
    public static final String FAIL_MATCH_ID_TOPIC = "failMatchId";
    public static final String SUCCESS_MATCH_TOPIC = "successMatch";
    public static final String REMAIN_MATCH_ID_RANGE_TOPIC = "remainMatchIdRange";
    public static final String LAST_MATCH_ID_TOPIC = "lastMatchId";
}
