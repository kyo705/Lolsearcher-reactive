package com.lolsearcher.reactive.unit.service.kafka.producer.match.lastMatchId;

import com.lolsearcher.reactive.exception.exception.InvalidMethodCallException;
import com.lolsearcher.reactive.service.kafka.match.LastMatchIdProducerService;
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

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.LAST_MATCH_ID_TOPIC;

@ExtendWith(MockitoExtension.class)
public class LastMatchIdProducerServiceTest {

    @Mock private ReactiveKafkaProducerTemplate<String, String> template;
    private LastMatchIdProducerService lastMatchIdProducerService;

    @BeforeEach
    public void setup(){
        lastMatchIdProducerService = new LastMatchIdProducerService(template);
    }

    @DisplayName("레코드가 정상적으로 카프카에 저장될 경우 플로우가 성공적으로 마무리된다.")
    @Test
    public void sendRecordWithSuccess(){

        //given
        String key = LastMatchIdProducerServiceTestSetup.getKey();
        String record = LastMatchIdProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = LastMatchIdProducerServiceTestSetup.getSenderResultWithSuccess();
        BDDMockito.given(template.send(LAST_MATCH_ID_TOPIC, key, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(lastMatchIdProducerService.sendRecord(key, record))
                .expectComplete()
                .verify();
    }

    @DisplayName("레코드가 카프카에 저장 실패한 경우 예외가 발생한다.")
    @Test
    public void sendRecordWithError(){

        //given
        String key = LastMatchIdProducerServiceTestSetup.getKey();
        String record = LastMatchIdProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = LastMatchIdProducerServiceTestSetup.getSenderResultWithFail();
        BDDMockito.given(template.send(LAST_MATCH_ID_TOPIC, key, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(lastMatchIdProducerService.sendRecord(key, record))
                .expectError()
                .verify();
    }

    @DisplayName("카프카에 레코드를 키 지정없이 저장 시도하는 경우 예외가 발생한다.")
    @Test
    public void sendRecordWithNoKey(){

        //given
        String record = LastMatchIdProducerServiceTestSetup.getRecord();

        //when & then
        StepVerifier.create(lastMatchIdProducerService.sendRecord(record))
                .expectError(InvalidMethodCallException.class)
                .verify();
    }
}
