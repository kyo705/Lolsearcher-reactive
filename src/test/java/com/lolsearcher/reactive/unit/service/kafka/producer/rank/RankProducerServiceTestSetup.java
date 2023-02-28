package com.lolsearcher.reactive.unit.service.kafka.producer.rank;

import com.lolsearcher.reactive.constant.constant.LolSearcherConstants;
import com.lolsearcher.reactive.model.entity.rank.Rank;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.kafka.sender.SenderResult;

public class RankProducerServiceTestSetup {

    protected static Rank getRecord() {

        return Rank.builder()
                .summonerId("summonerId")
                .seasonId(LolSearcherConstants.CURRENT_SEASON_ID)
                .queueType(LolSearcherConstants.SOLO_RANK)
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
