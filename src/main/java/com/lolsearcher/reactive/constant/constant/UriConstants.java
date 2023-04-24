package com.lolsearcher.reactive.constant.constant;

public class UriConstants {

    public static final String LOLSEARCHER_FRONT_SERVER_URI = "localhost";
    public static final String RIOTGAMES_SUMMONER_WITH_ID_URI = "/lol/summoner/v4/summoners/{id}?api_key={key}";
    public static final String RIOTGAMES_SUMMONER_WITH_NAME_URI = "/lol/summoner/v4/summoners/by-name/{name}?api_key={key}";
    public static final String RIOTGAMES_RANK_WITH_ID_URI = "/lol/league/v4/entries/by-summoner/{id}?api_key={key}";
    public static final String RIOTGAMES_MATCHIDS_WITH_PUUID_URI = "/lol/match/v5/matches/by-puuid/{puuid}/ids?start={start}&count={count}&api_key={key}";
    public static final String RIOTGAMES_MATCH_WITH_ID_URI = "/lol/match/v5/matches/{matchId}?api_key={key}";
    public static final String RIOTGAMES_INGAME_WITH_ID_URI = "/lol/spectator/v4/active-games/by-summoner/{id}?api_key={key}";
    public static final String JWT_RECREATION_URI = "http://localhost:8082/jwt";
    public static final String CERTIFICATION_SERVER_TOKEN_VALIDATION_URI = "http://localhost:8082/jwt?token={token}";
}
