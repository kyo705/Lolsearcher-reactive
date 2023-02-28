package com.lolsearcher.reactive.unit.service.kafka.producer.match.failMatchId;

import com.lolsearcher.reactive.constant.constant.KafkaConstant;
import com.lolsearcher.reactive.service.kafka.match.FailMatchIdProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class FailMatchIdProducerServiceTest {

    @Mock private ReactiveKafkaProducerTemplate<String, String> template;
    private FailMatchIdProducerService failMatchIdProducerService;

    @BeforeEach
    public void setup(){
        failMatchIdProducerService = new FailMatchIdProducerService(template);
    }

    @DisplayName("카프카에 정상적으로 레코드가 저장될 경우 플로우가 성공적으로 마무리된다.")
    @Test
    public void sendRecordTestWithSuccess(){

        //given
        String record = FailMatchIdProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = FailMatchIdProducerServiceTestSetup.getSendResultWithSuccess();
        BDDMockito.given(template.send(KafkaConstant.FAIL_MATCH_ID_TOPIC, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(failMatchIdProducerService.sendRecord(record))
                .expectComplete()
                .verify();
    }

    @DisplayName("카프카에 레코드가 저장되지 못한 경우 예외가 발생한다.")
    @Test
    public void sendRecordTestWithErrorToSaveKafka(){

        //given
        String record = FailMatchIdProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = FailMatchIdProducerServiceTestSetup.getSendResultWithFail();
        BDDMockito.given(template.send(KafkaConstant.FAIL_MATCH_ID_TOPIC, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(failMatchIdProducerService.sendRecord(record))
                .expectError()
                .verify();
    }
}
