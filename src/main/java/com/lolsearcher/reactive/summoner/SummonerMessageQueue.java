package com.lolsearcher.reactive.summoner;

public interface SummonerMessageQueue {

    void send(String key, SummonerDto summoner);

    void send(SummonerDto summoner);
}
