package com.lolsearcher.reactive.unit.service.kafka.producer.match.successMatch;

import com.lolsearcher.reactive.constant.enumeration.GameType;
import com.lolsearcher.reactive.model.entity.match.Match;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.kafka.sender.SenderResult;

public class SuccessMatchProducerServiceTestSetup {

    protected static Match getRecord() {

        Match match = new Match();
        match.setMatchId("matchId1");
        match.setQueueId(GameType.SOLO_RANK_MODE.getQueueId());

        return match;
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
