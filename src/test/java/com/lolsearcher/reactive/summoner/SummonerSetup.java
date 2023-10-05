package com.lolsearcher.reactive.summoner;

import java.util.List;

public class SummonerSetup {
    public static String getRiotGamesSummonerDto() {

        return "{\n" +
                "    \"id\": \"vI8IEER7jJGbdMOw6_1ciINz60FHxhL2jIMJY1SyCO_Bucw\",\n" +
                "    \"accountId\": \"kDdJvX-i6M5oVt5xZG57UPtRja7rXU5Bi3D5ZeBK-omSMPo\",\n" +
                "    \"puuid\": \"ROxYq8Jn3uGRmcgPCx0SoSsgqadU9xSpYs82XufITTL7y4ozdlvVbA2vwc6SXnaRGEnGYhL8BLQRrA\",\n" +
                "    \"name\": \"푸켓푸켓\",\n" +
                "    \"profileIconId\": 1231,\n" +
                "    \"revisionDate\": 1682144385000,\n" +
                "    \"summonerLevel\": 397\n" +
                "}";
    }

    public static String validSummonerUpdateRequest() {

        return "{\n" +
                "    \"summonerIds\" : [\n" +
                "        \"summonerId1\",\n" +
                "        \"summonerId2\",\n" +
                "        \"summonerId3\"\n" +
                "    ]\n" +
                "}";
    }

    public static List<String> getRiotGamesSummoners() {

        return List.of(
                "{\n" +
                        "    \"id\": \"summonerId1\",\n" +
                        "    \"accountId\": \"kDdJvX-i6M5oVt5xZG57UPtRja7rXU5Bi3D5ZeBK-omSMPo\",\n" +
                        "    \"puuid\": \"ROxYq8Jn3uGRmcgPCx0SoSsgqadU9xSpYs82XufITTL7y4ozdlvVbA2vwc6SXnaRGEnGYhL8BLQRrA\",\n" +
                        "    \"name\": \"롤하고싶다\",\n" +
                        "    \"profileIconId\": 1231,\n" +
                        "    \"revisionDate\": 1682144385000,\n" +
                        "    \"summonerLevel\": 397\n" +
                        "}",
                "{\n" +
                        "    \"id\": \"summonerId2\",\n" +
                        "    \"accountId\": \"kDdJvX-i6M5oVt5xZG57UPtRja7rXU5Bi3D5ZeBK-omSMPo\",\n" +
                        "    \"puuid\": \"ROxYq8Jn3uGRmcgPCx0SoSsgqadU9xSpYs82XufITTL7y4ozdlvVbA2vwc6SXnaRGEnGYhL8BLQRrA\",\n" +
                        "    \"name\": \"푸켓푸켓\",\n" +
                        "    \"profileIconId\": 1231,\n" +
                        "    \"revisionDate\": 1682144385000,\n" +
                        "    \"summonerLevel\": 397\n" +
                        "}",
                "{\n" +
                        "    \"id\": \"summonerId3\",\n" +
                        "    \"accountId\": \"kDdJvX-i6M5oVt5xZG57UPtRja7rXU5Bi3D5ZeBK-omSMPo\",\n" +
                        "    \"puuid\": \"ROxYq8Jn3uGRmcgPCx0SoSsgqadU9xSpYs82XufITTL7y4ozdlvVbA2vwc6SXnaRGEnGYhL8BLQRrA\",\n" +
                        "    \"name\": \"취업가즈아\",\n" +
                        "    \"profileIconId\": 1231,\n" +
                        "    \"revisionDate\": 1682144385000,\n" +
                        "    \"summonerLevel\": 397\n" +
                        "}"
        );
    }
}
