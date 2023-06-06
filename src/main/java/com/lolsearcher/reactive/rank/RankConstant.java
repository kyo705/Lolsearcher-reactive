package com.lolsearcher.reactive.rank;

public class RankConstant {

    public static final String RANK_URI = "/renew/summoner/{summonerId}/rank";
    public static final String RIOT_GAMES_RANK_WITH_ID_URI = "/lol/league/v4/entries/by-summoner/{id}?api_key={key}";
    public static final int LEAGUE_ID_MIN_LENGTH = 1;
    public static final int LEAGUE_ID_MAX_LENGTH = 50;
    public static final int THE_NUMBER_OF_RANK_TYPE = 2; /* 랭크 게임 종류 수 */
    public static final int CURRENT_SEASON_ID = 23;
    public static final int  INITIAL_SEASON_ID = 10;
    public static final String RANK_TEMPLATE = "rankTemplate";
    public static final String RANK_TOPIC = "rank";
    public static final String RANK_CACHE_KEY = "Rank";
}
