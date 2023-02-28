package com.lolsearcher.reactive.unit.service.kafka.producer.match.lastMatchId;

import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.kafka.sender.SenderResult;

public class LastMatchIdProducerServiceTestSetup {

    protected static String getKey() {
        return "summonerId";
    }

    protected static String getRecord() {
        return "lastMatchId";
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
