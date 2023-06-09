package com.lolsearcher.reactive.rank;

import java.util.stream.Stream;

public class RankSetup {

    protected static String getValidRanks() {

        return "[\n" +
                "    {\n" +
                "        \"leagueId\": \"6d733f7a-a02b-4156-ab25-ef1cf12d4de6\",\n" +
                "        \"queueType\": \"RANKED_FLEX_SR\",\n" +
                "        \"tier\": \"BRONZE\",\n" +
                "        \"rank\": \"III\",\n" +
                "        \"summonerId\": \"summonerId1\",\n" +
                "        \"summonerName\": \"비 오는 날 각성\",\n" +
                "        \"leaguePoints\": 43,\n" +
                "        \"wins\": 9,\n" +
                "        \"losses\": 9,\n" +
                "        \"veteran\": false,\n" +
                "        \"inactive\": false,\n" +
                "        \"freshBlood\": false,\n" +
                "        \"hotStreak\": false\n" +
                "    },\n" +
                "    {\n" +
                "        \"leagueId\": \"15f40db6-dd2e-48b4-aa3a-7d4b387dfcc8\",\n" +
                "        \"queueType\": \"RANKED_SOLO_5x5\",\n" +
                "        \"tier\": \"DIAMOND\",\n" +
                "        \"rank\": \"I\",\n" +
                "        \"summonerId\": \"summonerId1\",\n" +
                "        \"summonerName\": \"비 오는 날 각성\",\n" +
                "        \"leaguePoints\": 28,\n" +
                "        \"wins\": 292,\n" +
                "        \"losses\": 314,\n" +
                "        \"veteran\": false,\n" +
                "        \"inactive\": false,\n" +
                "        \"freshBlood\": false,\n" +
                "        \"hotStreak\": false\n" +
                "    }\n" +
                "]";
    }

    protected static Stream<String> invalidRanks() {

        return Stream.of(
                "[\n" +
                        "    {\n" +
                        "        \"leagueId\": \"6d733f7a-a02b-4156-ab25-ef1cf12d4de6\",\n" +
                        "        \"queueType\": \"RANKED_FLEX_SR\",\n" +
                        "        \"tier\": \"BRONZE\",\n" +
                        "        \"rank\": \"III\",\n" +
                        "        \"summonerId\": \"summonerId1\",\n" +
                        "        \"summonerName\": \"비 오는 날 각성\",\n" +
                        "        \"leaguePoints\": 43,\n" +
                        "        \"wins\": -9,\n" +
                        "        \"losses\": 9,\n" +
                        "        \"veteran\": false,\n" +
                        "        \"inactive\": false,\n" +
                        "        \"freshBlood\": false,\n" +
                        "        \"hotStreak\": false\n" +
                        "    }\n" +
                        "]",
                "[\n" +
                        "    {\n" +
                        "        \"leagueId\": \"6d733f7a-a02b-4156-ab25-ef1cf12d4de6\",\n" +
                        "        \"queueType\": \"RANKED_FLEX_SR\",\n" +
                        "        \"tier\": \"BRONZE\",\n" +
                        "        \"rank\": \"IIIII\",\n" +
                        "        \"summonerId\": \"summonerId1\",\n" +
                        "        \"summonerName\": \"비 오는 날 각성\",\n" +
                        "        \"leaguePoints\": 43,\n" +
                        "        \"wins\": -9,\n" +
                        "        \"losses\": 9,\n" +
                        "        \"veteran\": false,\n" +
                        "        \"inactive\": false,\n" +
                        "        \"freshBlood\": false,\n" +
                        "        \"hotStreak\": false\n" +
                        "    }\n" +
                        "]",
                "[\n" +
                        "    {\n" +
                        "        \"leagueId\": \"6d733f7a-a02b-4156-ab25-ef1cf12d4de6\",\n" +
                        "        \"queueType\": \"RANKED_FLEX_SR\",\n" +
                        "        \"tier\": \"브론즈\",\n" +
                        "        \"rank\": \"III\",\n" +
                        "        \"summonerId\": \"summonerId1\",\n" +
                        "        \"summonerName\": \"비 오는 날 각성\",\n" +
                        "        \"leaguePoints\": 43,\n" +
                        "        \"wins\": -9,\n" +
                        "        \"losses\": 9,\n" +
                        "        \"veteran\": false,\n" +
                        "        \"inactive\": false,\n" +
                        "        \"freshBlood\": false,\n" +
                        "        \"hotStreak\": false\n" +
                        "    }\n" +
                        "]",
                "[\n" +
                        "    {\n" +
                        "        \"leagueId\": \"6d733f7a-a02b-4156-ab25-ef1cf12d4de6\",\n" +
                        "        \"queueType\": \"INVALID_RANK\",\n" +
                        "        \"tier\": \"BRONZE\",\n" +
                        "        \"rank\": \"III\",\n" +
                        "        \"summonerId\": \"summonerId1\",\n" +
                        "        \"summonerName\": \"비 오는 날 각성\",\n" +
                        "        \"leaguePoints\": 43,\n" +
                        "        \"wins\": -9,\n" +
                        "        \"losses\": 9,\n" +
                        "        \"veteran\": false,\n" +
                        "        \"inactive\": false,\n" +
                        "        \"freshBlood\": false,\n" +
                        "        \"hotStreak\": false\n" +
                        "    }\n" +
                        "]"
        );
    }
}
