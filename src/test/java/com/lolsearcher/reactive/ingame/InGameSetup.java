package com.lolsearcher.reactive.ingame;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.*;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getSpellKey;

public class InGameSetup {
    protected static String getValidRiotGamesInGameDto() {

        return "{\n" +
                "    \"gameId\": 6534295851,\n" +
                "    \"mapId\": 11,\n" +
                "    \"gameMode\": \"CLASSIC\",\n" +
                "    \"gameType\": \"MATCHED_GAME\",\n" +
                "    \"gameQueueConfigId\": 420,\n" +
                "    \"participants\": [\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 14,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 43,\n" +
                "            \"profileIconId\": 977,\n" +
                "            \"summonerName\": \"HLE Academy\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8229,\n" +
                "                    8275,\n" +
                "                    8233,\n" +
                "                    8237,\n" +
                "                    8345,\n" +
                "                    8347,\n" +
                "                    5008,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8200,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 14,\n" +
                "            \"championId\": 78,\n" +
                "            \"profileIconId\": 5840,\n" +
                "            \"summonerName\": \"OMGAbIe\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"55GtwCCyoQnSX7yBFmgfZDvsooH4i1XYgcle1yhn_NCIqJIfRGxy4Qmr9g\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8437,\n" +
                "                    8401,\n" +
                "                    8444,\n" +
                "                    8451,\n" +
                "                    8345,\n" +
                "                    8352,\n" +
                "                    5005,\n" +
                "                    5008,\n" +
                "                    5003\n" +
                "                ],\n" +
                "                \"perkStyle\": 8400,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 11,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 76,\n" +
                "            \"profileIconId\": 5662,\n" +
                "            \"summonerName\": \"Blank\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"NvfIrYA0N8h-lZ__AfYXZdkLiU1w9UkKQ4t3yqAvnsi5K0w\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8128,\n" +
                "                    8143,\n" +
                "                    8138,\n" +
                "                    8105,\n" +
                "                    8210,\n" +
                "                    8232,\n" +
                "                    5005,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8100,\n" +
                "                \"perkSubStyle\": 8200\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 12,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 897,\n" +
                "            \"profileIconId\": 4302,\n" +
                "            \"summonerName\": \"Bae Suzy\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"tS7x_TwofR9fpvcocYhSPAfAjMDQxD-q67pbKGT7eOtJlebqf1njTKT06A\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8437,\n" +
                "                    8446,\n" +
                "                    8444,\n" +
                "                    8451,\n" +
                "                    8347,\n" +
                "                    8345,\n" +
                "                    5005,\n" +
                "                    5003,\n" +
                "                    5003\n" +
                "                ],\n" +
                "                \"perkStyle\": 8400,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 7,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 21,\n" +
                "            \"profileIconId\": 5271,\n" +
                "            \"summonerName\": \"부끄러운스승3\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"QeEfY60ym8M_eqBN7E7CQsCw1TNZj4CAQiAtI2SUkYVrbvA\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8229,\n" +
                "                    8226,\n" +
                "                    8233,\n" +
                "                    8237,\n" +
                "                    8126,\n" +
                "                    8105,\n" +
                "                    5008,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8200,\n" +
                "                \"perkSubStyle\": 8100\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 6,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 8,\n" +
                "            \"profileIconId\": 5369,\n" +
                "            \"summonerName\": \"Bruno Lars\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"Ne1zcFFjpt1-doP7diGeBCbiXo7yZLr1Kea-xkN8qvjygOmc-XIqIsAKJg\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8230,\n" +
                "                    8275,\n" +
                "                    8210,\n" +
                "                    8236,\n" +
                "                    8347,\n" +
                "                    8304,\n" +
                "                    5007,\n" +
                "                    5008,\n" +
                "                    5001\n" +
                "                ],\n" +
                "                \"perkStyle\": 8200,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 12,\n" +
                "            \"championId\": 79,\n" +
                "            \"profileIconId\": 3545,\n" +
                "            \"summonerName\": \"08midmid\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"UG0uGhQ5YFg1XFDZDzsdBbfJPG9iHcYz5wulo7E1dHnDBHM\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8230,\n" +
                "                    8226,\n" +
                "                    8210,\n" +
                "                    8237,\n" +
                "                    8345,\n" +
                "                    8347,\n" +
                "                    5007,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8200,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 14,\n" +
                "            \"championId\": 497,\n" +
                "            \"profileIconId\": 25,\n" +
                "            \"summonerName\": \"KDF 0din\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"Mcxz0aF3LJkfFlAAXhuLn-2CUNHFWLyxLFVn9Kpb2PAHR1aB1Hj-G-JnDw\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8465,\n" +
                "                    8463,\n" +
                "                    8444,\n" +
                "                    8242,\n" +
                "                    8136,\n" +
                "                    8106,\n" +
                "                    5008,\n" +
                "                    5008,\n" +
                "                    5003\n" +
                "                ],\n" +
                "                \"perkStyle\": 8400,\n" +
                "                \"perkSubStyle\": 8100\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 11,\n" +
                "            \"championId\": 203,\n" +
                "            \"profileIconId\": 7,\n" +
                "            \"summonerName\": \"eightQAQ\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"T5mZPMSDCOdFSFX67irEUn5LcByuX_w28lDR0BPfe1VPEBliFCf7LgaagA\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8010,\n" +
                "                    9111,\n" +
                "                    9104,\n" +
                "                    8014,\n" +
                "                    8304,\n" +
                "                    8347,\n" +
                "                    5005,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8000,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 6,\n" +
                "            \"championId\": 895,\n" +
                "            \"profileIconId\": 5460,\n" +
                "            \"summonerName\": \"drx4\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"qRjYNz2IvQLvL7aibJSSkHAsvTQfZRyOV81e38_xEu7C6GU\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8010,\n" +
                "                    9111,\n" +
                "                    9103,\n" +
                "                    8299,\n" +
                "                    8304,\n" +
                "                    8345,\n" +
                "                    5005,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8000,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"observers\": {\n" +
                "        \"encryptionKey\": \"IqEqKSlXBGYSgXi3gGbuhpWmojakTaE9\"\n" +
                "    },\n" +
                "    \"platformId\": \"KR\",\n" +
                "    \"bannedChampions\": [\n" +
                "        {\n" +
                "            \"championId\": 104,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 236,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 2\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 81,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 3\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": -1,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 4\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 555,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 5\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 60,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 6\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 902,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 7\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 518,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 8\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 121,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 9\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 421,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 10\n" +
                "        }\n" +
                "    ],\n" +
                "    \"gameStartTime\": 1686284996681,\n" +
                "    \"gameLength\": 906\n" +
                "}";
    }


    protected static Stream<String> getInvalidRiotGamesInGameDto() {

        return Stream.of(
                "{\n" +
                "    \"gameId\": 6534295851,\n" +
                "    \"mapId\": 11,\n" +
                "    \"gameMode\": \"CLASSIC\",\n" +
                "    \"gameType\": \"MATCHED_GAME\",\n" +
                "    \"gameQueueConfigId\": \"뭐야\",\n" +
                "    \"participants\": [\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 14,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 43,\n" +
                "            \"profileIconId\": 977,\n" +
                "            \"summonerName\": \"HLE Academy\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8229,\n" +
                "                    8275,\n" +
                "                    8233,\n" +
                "                    8237,\n" +
                "                    8345,\n" +
                "                    8347,\n" +
                "                    5008,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8200,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 14,\n" +
                "            \"championId\": 78,\n" +
                "            \"profileIconId\": 5840,\n" +
                "            \"summonerName\": \"OMGAbIe\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"55GtwCCyoQnSX7yBFmgfZDvsooH4i1XYgcle1yhn_NCIqJIfRGxy4Qmr9g\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8437,\n" +
                "                    8401,\n" +
                "                    8444,\n" +
                "                    8451,\n" +
                "                    8345,\n" +
                "                    8352,\n" +
                "                    5005,\n" +
                "                    5008,\n" +
                "                    5003\n" +
                "                ],\n" +
                "                \"perkStyle\": 8400,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 11,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 76,\n" +
                "            \"profileIconId\": 5662,\n" +
                "            \"summonerName\": \"Blank\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"NvfIrYA0N8h-lZ__AfYXZdkLiU1w9UkKQ4t3yqAvnsi5K0w\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8128,\n" +
                "                    8143,\n" +
                "                    8138,\n" +
                "                    8105,\n" +
                "                    8210,\n" +
                "                    8232,\n" +
                "                    5005,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8100,\n" +
                "                \"perkSubStyle\": 8200\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 12,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 897,\n" +
                "            \"profileIconId\": 4302,\n" +
                "            \"summonerName\": \"Bae Suzy\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"tS7x_TwofR9fpvcocYhSPAfAjMDQxD-q67pbKGT7eOtJlebqf1njTKT06A\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8437,\n" +
                "                    8446,\n" +
                "                    8444,\n" +
                "                    8451,\n" +
                "                    8347,\n" +
                "                    8345,\n" +
                "                    5005,\n" +
                "                    5003,\n" +
                "                    5003\n" +
                "                ],\n" +
                "                \"perkStyle\": 8400,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 100,\n" +
                "            \"spell1Id\": 7,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 21,\n" +
                "            \"profileIconId\": 5271,\n" +
                "            \"summonerName\": \"부끄러운스승3\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"QeEfY60ym8M_eqBN7E7CQsCw1TNZj4CAQiAtI2SUkYVrbvA\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8229,\n" +
                "                    8226,\n" +
                "                    8233,\n" +
                "                    8237,\n" +
                "                    8126,\n" +
                "                    8105,\n" +
                "                    5008,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8200,\n" +
                "                \"perkSubStyle\": 8100\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 6,\n" +
                "            \"spell2Id\": 4,\n" +
                "            \"championId\": 8,\n" +
                "            \"profileIconId\": 5369,\n" +
                "            \"summonerName\": \"Bruno Lars\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"Ne1zcFFjpt1-doP7diGeBCbiXo7yZLr1Kea-xkN8qvjygOmc-XIqIsAKJg\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8230,\n" +
                "                    8275,\n" +
                "                    8210,\n" +
                "                    8236,\n" +
                "                    8347,\n" +
                "                    8304,\n" +
                "                    5007,\n" +
                "                    5008,\n" +
                "                    5001\n" +
                "                ],\n" +
                "                \"perkStyle\": 8200,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 12,\n" +
                "            \"championId\": 79,\n" +
                "            \"profileIconId\": 3545,\n" +
                "            \"summonerName\": \"08midmid\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"UG0uGhQ5YFg1XFDZDzsdBbfJPG9iHcYz5wulo7E1dHnDBHM\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8230,\n" +
                "                    8226,\n" +
                "                    8210,\n" +
                "                    8237,\n" +
                "                    8345,\n" +
                "                    8347,\n" +
                "                    5007,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8200,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 14,\n" +
                "            \"championId\": 497,\n" +
                "            \"profileIconId\": 25,\n" +
                "            \"summonerName\": \"KDF 0din\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"Mcxz0aF3LJkfFlAAXhuLn-2CUNHFWLyxLFVn9Kpb2PAHR1aB1Hj-G-JnDw\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8465,\n" +
                "                    8463,\n" +
                "                    8444,\n" +
                "                    8242,\n" +
                "                    8136,\n" +
                "                    8106,\n" +
                "                    5008,\n" +
                "                    5008,\n" +
                "                    5003\n" +
                "                ],\n" +
                "                \"perkStyle\": 8400,\n" +
                "                \"perkSubStyle\": 8100\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 11,\n" +
                "            \"championId\": 203,\n" +
                "            \"profileIconId\": 7,\n" +
                "            \"summonerName\": \"eightQAQ\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"T5mZPMSDCOdFSFX67irEUn5LcByuX_w28lDR0BPfe1VPEBliFCf7LgaagA\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8010,\n" +
                "                    9111,\n" +
                "                    9104,\n" +
                "                    8014,\n" +
                "                    8304,\n" +
                "                    8347,\n" +
                "                    5005,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8000,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"teamId\": 200,\n" +
                "            \"spell1Id\": 4,\n" +
                "            \"spell2Id\": 6,\n" +
                "            \"championId\": 895,\n" +
                "            \"profileIconId\": 5460,\n" +
                "            \"summonerName\": \"drx4\",\n" +
                "            \"bot\": false,\n" +
                "            \"summonerId\": \"qRjYNz2IvQLvL7aibJSSkHAsvTQfZRyOV81e38_xEu7C6GU\",\n" +
                "            \"gameCustomizationObjects\": [],\n" +
                "            \"perks\": {\n" +
                "                \"perkIds\": [\n" +
                "                    8010,\n" +
                "                    9111,\n" +
                "                    9103,\n" +
                "                    8299,\n" +
                "                    8304,\n" +
                "                    8345,\n" +
                "                    5005,\n" +
                "                    5008,\n" +
                "                    5002\n" +
                "                ],\n" +
                "                \"perkStyle\": 8000,\n" +
                "                \"perkSubStyle\": 8300\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"observers\": {\n" +
                "        \"encryptionKey\": \"IqEqKSlXBGYSgXi3gGbuhpWmojakTaE9\"\n" +
                "    },\n" +
                "    \"platformId\": \"KR\",\n" +
                "    \"bannedChampions\": [\n" +
                "        {\n" +
                "            \"championId\": 104,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 236,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 2\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 81,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 3\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": -1,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 4\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 555,\n" +
                "            \"teamId\": 100,\n" +
                "            \"pickTurn\": 5\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 60,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 6\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 902,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 7\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 518,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 8\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 121,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 9\n" +
                "        },\n" +
                "        {\n" +
                "            \"championId\": 421,\n" +
                "            \"teamId\": 200,\n" +
                "            \"pickTurn\": 10\n" +
                "        }\n" +
                "    ],\n" +
                "    \"gameStartTime\": 1686284996681,\n" +
                "    \"gameLength\": 906\n" +
                "}"
        );
    }

    protected static Map<String, String> setupCache() {

        Map<String, String> setupCache = new HashMap<>();

        setupCache.put(getQueueKey(420L), " ");

        setupCache.put(getChampionKey(43L), " ");
        setupCache.put(getChampionKey(76L), " ");
        setupCache.put(getChampionKey(897L), " ");
        setupCache.put(getChampionKey(21L), " ");
        setupCache.put(getChampionKey(8L), " ");
        setupCache.put(getChampionKey(79L), " ");
        setupCache.put(getChampionKey(497L), " ");
        setupCache.put(getChampionKey(203L), " ");
        setupCache.put(getChampionKey(895L), " ");
        setupCache.put(getChampionKey(104L), " ");
        setupCache.put(getChampionKey(236L), " ");
        setupCache.put(getChampionKey(-1L), " ");
        setupCache.put(getChampionKey(555L), " ");
        setupCache.put(getChampionKey(60L), " ");
        setupCache.put(getChampionKey(902L), " ");
        setupCache.put(getChampionKey(518L), " ");
        setupCache.put(getChampionKey(121L), " ");
        setupCache.put(getChampionKey(421L), " ");
        setupCache.put(getChampionKey(81L), " ");
        setupCache.put(getChampionKey(78L), " ");

        setupCache.put(getRuneKey((short) 8010), " ");
        setupCache.put(getRuneKey((short) 9111), " ");
        setupCache.put(getRuneKey((short) 9103), " ");
        setupCache.put(getRuneKey((short) 9104), " ");
        setupCache.put(getRuneKey((short) 8299), " ");
        setupCache.put(getRuneKey((short) 8014), " ");
        setupCache.put(getRuneKey((short) 8304), " ");
        setupCache.put(getRuneKey((short) 8345), " ");
        setupCache.put(getRuneKey((short) 8347), " ");
        setupCache.put(getRuneKey((short) 8465), " ");
        setupCache.put(getRuneKey((short) 8463), " ");
        setupCache.put(getRuneKey((short) 8444), " ");
        setupCache.put(getRuneKey((short) 8242), " ");
        setupCache.put(getRuneKey((short) 8136), " ");
        setupCache.put(getRuneKey((short) 8106), " ");
        setupCache.put(getRuneKey((short) 8230), " ");
        setupCache.put(getRuneKey((short) 8226), " ");
        setupCache.put(getRuneKey((short) 8210), " ");
        setupCache.put(getRuneKey((short) 8237), " ");
        setupCache.put(getRuneKey((short) 8230), " ");
        setupCache.put(getRuneKey((short) 8275), " ");
        setupCache.put(getRuneKey((short) 8210), " ");
        setupCache.put(getRuneKey((short) 8210), " ");
        setupCache.put(getRuneKey((short) 8236), " ");
        setupCache.put(getRuneKey((short) 8347), " ");
        setupCache.put(getRuneKey((short) 8304), " ");
        setupCache.put(getRuneKey((short) 8229), " ");
        setupCache.put(getRuneKey((short) 8226), " ");
        setupCache.put(getRuneKey((short) 8233), " ");
        setupCache.put(getRuneKey((short) 8237), " ");
        setupCache.put(getRuneKey((short) 8126), " ");
        setupCache.put(getRuneKey((short) 8105), " ");
        setupCache.put(getRuneKey((short) 8437), " ");
        setupCache.put(getRuneKey((short) 8446), " ");
        setupCache.put(getRuneKey((short) 8444), " ");
        setupCache.put(getRuneKey((short) 8451), " ");
        setupCache.put(getRuneKey((short) 8347), " ");
        setupCache.put(getRuneKey((short) 8345), " ");
        setupCache.put(getRuneKey((short) 8128), " ");
        setupCache.put(getRuneKey((short) 8143), " ");
        setupCache.put(getRuneKey((short) 8138), " ");
        setupCache.put(getRuneKey((short) 8105), " ");
        setupCache.put(getRuneKey((short) 8210), " ");
        setupCache.put(getRuneKey((short) 8232), " ");
        setupCache.put(getRuneKey((short) 8437), " ");
        setupCache.put(getRuneKey((short) 8401), " ");
        setupCache.put(getRuneKey((short) 8444), " ");
        setupCache.put(getRuneKey((short) 8451), " ");
        setupCache.put(getRuneKey((short) 8345), " ");
        setupCache.put(getRuneKey((short) 8352), " ");
        setupCache.put(getRuneKey((short) 8229), " ");
        setupCache.put(getRuneKey((short) 8275), " ");
        setupCache.put(getRuneKey((short) 8233), " ");
        setupCache.put(getRuneKey((short) 8237), " ");
        setupCache.put(getRuneKey((short) 8345), " ");
        setupCache.put(getRuneKey((short) 8347), " ");

        setupCache.put(getRuneKey((short) 5005), " ");
        setupCache.put(getRuneKey((short) 5008), " ");
        setupCache.put(getRuneKey((short) 5002), " ");
        setupCache.put(getRuneKey((short) 5003), " ");
        setupCache.put(getRuneKey((short) 5007), " ");
        setupCache.put(getRuneKey((short) 5001), " ");
        setupCache.put(getRuneKey((short) 5002), " ");

        setupCache.put(getRuneKey((short) 8000), " ");
        setupCache.put(getRuneKey((short) 8300), " ");
        setupCache.put(getRuneKey((short) 8400), " ");
        setupCache.put(getRuneKey((short) 8100), " ");
        setupCache.put(getRuneKey((short) 8200), " ");

        setupCache.put(getSpellKey(14), " ");
        setupCache.put(getSpellKey(4), " ");
        setupCache.put(getSpellKey(12), " ");
        setupCache.put(getSpellKey(7), " ");
        setupCache.put(getSpellKey(6), " ");
        setupCache.put(getSpellKey(11), " ");

        return setupCache;
    }
}
