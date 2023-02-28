package com.lolsearcher.reactive.unit.service.kafka.producer.summoner;

import com.lolsearcher.reactive.model.entity.summoner.Summoner;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.kafka.sender.SenderResult;

public class SummonerProducerServiceTestSetup {

    protected static Summoner getRecord() {

        return Summoner.builder()
                .summonerId("summonerId")
                .summonerName("닉네임")
                .puuid("puuid")
                .build();
    }

    protected static SenderResult<Void> getSenderResultWithSuccess() {

        return new SenderResult<Void>() {
            @Override
            public RecordMetadata recordMetadata() {
                return null;
            }

            @Override
            public Exception exception() {
                return null;
            }

            @Override
            public Void correlationMetadata() {
                return null;
            }
        };
    }

    protected static SenderResult<Void> getSenderResultWithFail() {

        return new SenderResult<Void>() {
            @Override
            public RecordMetadata recordMetadata() {
                return null;
            }

            @Override
            public Exception exception() {
                return new RuntimeException();
            }

            @Override
            public Void correlationMetadata() {
                return null;
            }
        };
    }
}
