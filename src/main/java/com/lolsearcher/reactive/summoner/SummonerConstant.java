package com.lolsearcher.reactive.summoner;

public class SummonerConstant {


    public static final int ACCOUNT_ID_MIN_LENGTH = 1;
    public static final int ACCOUNT_ID_MAX_LENGTH = 56;
    public static final int PUUID_MIN_LENGTH = 1;
    public static final int PUUID_MAX_LENGTH = 78;
    public static final int SUMMONER_ID_MIN_LENGTH = 1;
    public static final int SUMMONER_ID_MAX_LENGTH = 63;
    public static final int SUMMONER_NAME_MIN_LENGTH = 1;
    public static final int SUMMONER_NAME_MAX_LENGTH = 50;
    public static final String RIOTGAMES_SUMMONER_WITH_ID_URI = "/lol/summoner/v4/summoners/{id}?api_key={key}";
    public static final String RIOTGAMES_SUMMONER_WITH_NAME_URI = "/lol/summoner/v4/summoners/by-name/{name}?api_key={key}";
    public static final String SUMMONER_TEMPLATE = "summonerTemplate";
    public static final String SUMMONER_TOPIC = "topic:summoner";
    public static final String SUMMONER_CACHE_KEY = "Summoner";
}
