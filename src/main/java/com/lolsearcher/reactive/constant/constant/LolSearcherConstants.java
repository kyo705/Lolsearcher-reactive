package com.lolsearcher.reactive.constant.constant;

public class LolSearcherConstants {
    public static final int MAX_SUMMONER_NAME_LENGTH = 50;
    public static final String REGEX = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]"; //문자,숫자 빼고 다 필터링(띄어쓰기 포함)
    public static final long SUMMONER_RENEW_MS = 5*60*1000; //5min

    public static final int THE_NUMBER_OF_RANK_TYPE = 2; /* 랭크 게임 종류 수 */

    public static final int THE_NUMBER_OF_TEAMS = 2; /* Match Entity에 포함될 Team 수 */
    public static final int THE_NUMBER_OF_TEAM_MEMBERS = 5; /* Team Entity에 포함될 Member 수 */

    public static final int MATCH_DEFAULT_COUNT = 20; /* 유저 최신 전적 가져오는 갯수 */
    public static final int MATCH_ID_DEFAULT_COUNT = 100; /* REST API 요청시 응답받을 matchId 갯수 */
    public static final int MOST_CHAMP_LIMITED_COUNT = 10; /*유저 모스트 챔피언 가져오는 갯수 */
    public static final int LOGIN_BAN_COUNT = 10;
    public static final int SEARCH_BAN_COUNT = 20;

    public static final int CURRENT_SEASON_ID = 22;
    public static final String SOLO_RANK = "RANKED_SOLO_5x5";
    public static final String FLEX_RANK = "RANKED_FLEX_SR";

    public static final String MATCH_DATA_VERSION = "2";
    public static final String ALL = "all";
    public static final String FORWARDED_HTTP_HEADER = "X-Forwarded-For";
    public static final String VALID = "valid";
    public static final CharSequence RENEW_PATH = "/renew";
    public static final String FRONT_SERVER_URI = "localhost:80";
}
