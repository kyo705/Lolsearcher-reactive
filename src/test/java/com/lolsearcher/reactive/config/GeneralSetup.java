package com.lolsearcher.reactive.config;

import java.util.HashMap;
import java.util.Map;

import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.*;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getSpellKey;

public class GeneralSetup {

    protected static Map<String, String> setupCache() {

        //champion id, item id, rune id, spell id, queue id 등을 기존 캐시에 세팅
        // value 값은 간단하게 표시하기 위해 blank로 표현(기존 value는 champion name, item name 등임)

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

        setupCache.put(getItemKey(2033), " ");
        setupCache.put(getItemKey(2010), " ");
        setupCache.put(getItemKey(6665), " ");
        setupCache.put(getItemKey(3111), " ");
        setupCache.put(getItemKey(8020), " ");
        setupCache.put(getItemKey(0), " ");
        setupCache.put(getItemKey(3364), " ");

        setupCache.put(getItemKey(3145), " ");
        setupCache.put(getItemKey(3916), " ");
        setupCache.put(getItemKey(2055), " ");
        setupCache.put(getItemKey(1082), " ");
        setupCache.put(getItemKey(3158), " ");
        setupCache.put(getItemKey(4636), " ");
        setupCache.put(getItemKey(3364), " ");

        setupCache.put(getItemKey(6665), " ");
        setupCache.put(getItemKey(3068), " ");
        setupCache.put(getItemKey(2055), " ");
        setupCache.put(getItemKey(3082), " ");
        setupCache.put(getItemKey(1054), " ");
        setupCache.put(getItemKey(3111), " ");
        setupCache.put(getItemKey(3364), " ");

        setupCache.put(getItemKey(3142), " ");
        setupCache.put(getItemKey(1056), " ");
        setupCache.put(getItemKey(3814), " ");
        setupCache.put(getItemKey(3340), " ");

        setupCache.put(getItemKey(2031), " ");
        setupCache.put(getItemKey(3853), " ");
        setupCache.put(getItemKey(2065), " ");
        setupCache.put(getItemKey(2055), " ");
        setupCache.put(getItemKey(3916), " ");
        setupCache.put(getItemKey(3158), " ");
        setupCache.put(getItemKey(3364), " ");

        setupCache.put(getItemKey(2031), " ");
        setupCache.put(getItemKey(6656), " ");
        setupCache.put(getItemKey(2420), " ");
        setupCache.put(getItemKey(4629), " ");
        setupCache.put(getItemKey(3158), " ");
        setupCache.put(getItemKey(1082), " ");
        setupCache.put(getItemKey(3363), " ");

        setupCache.put(getItemKey(3111), " ");
        setupCache.put(getItemKey(6672), " ");
        setupCache.put(getItemKey(6676), " ");
        setupCache.put(getItemKey(3364), " ");

        setupCache.put(getItemKey(1052), " ");
        setupCache.put(getItemKey(3152), " ");
        setupCache.put(getItemKey(1056), " ");
        setupCache.put(getItemKey(3020), " ");

        setupCache.put(getItemKey(6671), " ");
        setupCache.put(getItemKey(3047), " ");
        setupCache.put(getItemKey(6676), " ");
        setupCache.put(getItemKey(1018), " ");
        setupCache.put(getItemKey(1055), " ");
        setupCache.put(getItemKey(3363), " ");

        setupCache.put(getItemKey(3860), " ");
        setupCache.put(getItemKey(2065), " ");
        setupCache.put(getItemKey(2055), " ");
        setupCache.put(getItemKey(3158), " ");
        setupCache.put(getItemKey(3023), " ");

        return setupCache;
    }
}
