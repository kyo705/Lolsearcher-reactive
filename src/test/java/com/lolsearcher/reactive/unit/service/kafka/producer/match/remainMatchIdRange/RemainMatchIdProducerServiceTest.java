package com.lolsearcher.reactive.unit.service.kafka.producer.match.remainMatchIdRange;

import com.lolsearcher.reactive.exception.exception.InvalidMethodCallException;
import com.lolsearcher.reactive.model.input.kafka.RemainMatchIdRange;
import com.lolsearcher.reactive.service.kafka.match.RemainMatchIdRangeProducerService;
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

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.REMAIN_MATCH_ID_RANGE_TOPIC;

@ExtendWith(MockitoExtension.class)
public class RemainMatchIdProducerServiceTest {

    @Mock private ReactiveKafkaProducerTemplate<String, RemainMatchIdRange> template;
    private RemainMatchIdRangeProducerService remainMatchIdRangeProducerService;

    @BeforeEach
    public void setup(){
        remainMatchIdRangeProducerService = new RemainMatchIdRangeProducerService(template);
    }

    @DisplayName("레코드가 정상적으로 카프카에 저장될 경우 성공적으로 플로우가 종료된다.")
    @Test
    public void sendRecordTestWithSuccess(){

        //given
        String key = RemainMatchIdProducerServiceTestSetup.getKey();
        RemainMatchIdRange record = RemainMatchIdProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = RemainMatchIdProducerServiceTestSetup.getSenderResultWithSuccess();
        BDDMockito.given(template.send(REMAIN_MATCH_ID_RANGE_TOPIC, key, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(remainMatchIdRangeProducerService.sendRecord(key, record))
                .expectComplete()
                .verify();
    }

    @DisplayName("레코드가 카프카에 저장 실패한 경우, 예외가 발생한다.")
    @Test
    public void sendRecordTestWithError(){

        //given
        String key = RemainMatchIdProducerServiceTestSetup.getKey();
        RemainMatchIdRange record = RemainMatchIdProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = RemainMatchIdProducerServiceTestSetup.getSenderResultWithFail();
        BDDMockito.given(template.send(REMAIN_MATCH_ID_RANGE_TOPIC, key, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(remainMatchIdRangeProducerService.sendRecord(key, record))
                .expectError()
                .verify();
    }

    @DisplayName("카프카에 키 없이 레코드를 저장 시도할 경우 예외가 발생한다.")
    @Test
    public void sendRecordTestWithNoKey(){

        //given
        RemainMatchIdRange record = RemainMatchIdProducerServiceTestSetup.getRecord();

        //when & then
        StepVerifier.create(remainMatchIdRangeProducerService.sendRecord(record))
                .expectError(InvalidMethodCallException.class)
                .verify();
    }
}
