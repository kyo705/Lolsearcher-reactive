package com.lolsearcher.reactive.rank;

public interface RankMessageQueue {

    void send(String key, RankDto rank);

    void send(RankDto rank);
}
