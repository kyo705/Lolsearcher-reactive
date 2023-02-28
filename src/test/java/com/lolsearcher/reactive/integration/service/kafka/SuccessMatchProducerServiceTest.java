package com.lolsearcher.reactive.integration.service.kafka;

import com.lolsearcher.reactive.model.entity.match.Match;
import com.lolsearcher.reactive.service.kafka.match.SuccessMatchProducerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@EmbeddedKafka(
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "offsets.topic.replication.factor=1",
                "transaction.state.log.replication.factor=1",
                "transaction.state.log.min.isr=1"
        }
)
public class SuccessMatchProducerServiceTest {

    @Autowired
    private SuccessMatchProducerService successMatchProducerService;

    @DisplayName("카프카로 전송할 데이터가 적절하고 카프카 연결 상태가 정상적이라면 카프카에 정상적으로 레코드가 저장된다.")
    @Test
    public void sendingSuccessMatchRecordWithSuccess() throws InterruptedException, IllegalAccessException {

        //given
        Match successMatch = KafkaServiceTestSetup.getMatch();

        //when
        successMatchProducerService.sendRecord(successMatch);
        Thread.sleep(1000);

        //then
        ConsumerRecords<String, Match> consumingSuccessMatchRecords = KafkaServiceTestSetup.consumeSuccessMatchRecord();

        assertThat(consumingSuccessMatchRecords.count()).isEqualTo(1);
        for (ConsumerRecord<String, Match> consumingSuccessMatch : consumingSuccessMatchRecords) {
            assertThat(consumingSuccessMatch.value().getMatchId()).isEqualTo(successMatch.getMatchId());
        }
    }
}
