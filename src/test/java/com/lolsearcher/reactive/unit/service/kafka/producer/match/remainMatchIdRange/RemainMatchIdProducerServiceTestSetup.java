package com.lolsearcher.reactive.unit.service.kafka.producer.match.remainMatchIdRange;

import com.lolsearcher.reactive.model.input.kafka.RemainMatchIdRange;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.kafka.sender.SenderResult;

public class RemainMatchIdProducerServiceTestSetup {

    protected static String getKey() {

        return "puuid";
    }

    protected static RemainMatchIdRange getRecord() {

        RemainMatchIdRange record = new RemainMatchIdRange();
        record.setEndRemainMatchId("endRemainMatchId");
        record.setStartRemainMatchId("startRemainMatchId");

        return record;
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
