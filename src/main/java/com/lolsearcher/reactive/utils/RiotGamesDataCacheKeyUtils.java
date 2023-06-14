package com.lolsearcher.reactive.utils;

public class RiotGamesDataCacheKeyUtils {

    private static final String CHAMPION_KEY_PREFIX = "cache:champion:";
    private static final String ITEM_KEY_PREFIX = "cache:item:";
    private static final String QUEUE_KEY_PREFIX = "cache:queue:";
    private static final String SPELL_KEY_PREFIX = "cache:spell:";
    private static final String RUNE_KEY_PREFIX = "cache:rune:";

    public static String getChampionKey(long championId) {

        return CHAMPION_KEY_PREFIX + championId;
    }

    public static String getItemKey(int item) {

        return ITEM_KEY_PREFIX + item;
    }

    public static String getQueueKey(long queueId) {

        return QUEUE_KEY_PREFIX + queueId;
    }

    public static String getSpellKey(int spell) {

        return SPELL_KEY_PREFIX + spell;
    }

    public static String getRuneKey(short rune) {

        return RUNE_KEY_PREFIX + rune;
    }

}
