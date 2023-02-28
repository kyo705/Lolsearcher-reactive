package com.lolsearcher.reactive.unit.service.kafka.producer.match.failMatchId;

import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.kafka.sender.SenderResult;

public class FailMatchIdProducerServiceTestSetup {

    protected static String getRecord() {

        return "failMatchId1";
    }

    protected static SenderResult<Void> getSendResultWithFail() {

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

    protected static SenderResult<Void> getSendResultWithSuccess() {

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
}
