package com.lolsearcher.reactive.unit.service.kafka.producer.match.successMatch;

import com.lolsearcher.reactive.constant.constant.KafkaConstant;
import com.lolsearcher.reactive.model.entity.match.Match;
import com.lolsearcher.reactive.service.kafka.match.SuccessMatchProducerService;
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
public class SuccessMatchProducerServiceTest {

    @Mock private ReactiveKafkaProducerTemplate<String, Match> template;
    private SuccessMatchProducerService successMatchProducerService;

    @BeforeEach
    public void setup(){
        successMatchProducerService = new SuccessMatchProducerService(template);
    }

    @DisplayName("카프카에 정상적으로 데이터가 저장될 경우 성공적으로 플로우가 종료된다.")
    @Test
    public void sendRecordWithSuccess(){

        //given
        Match record = SuccessMatchProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = SuccessMatchProducerServiceTestSetup.getSenderResultWithSuccess();
        BDDMockito.given(template.send(KafkaConstant.SUCCESS_MATCH_TOPIC, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(successMatchProducerService.sendRecord(record))
                .expectComplete()
                .verify();
    }

    @DisplayName("카프카에 데이터가 저장 실패할 경우 예외가 발생한다.")
    @Test
    public void sendRecordWithError(){

        //given
        Match record = SuccessMatchProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = SuccessMatchProducerServiceTestSetup.getSenderResultWithFail();
        BDDMockito.given(template.send(KafkaConstant.SUCCESS_MATCH_TOPIC, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(successMatchProducerService.sendRecord(record))
                .expectError()
                .verify();
    }
}
