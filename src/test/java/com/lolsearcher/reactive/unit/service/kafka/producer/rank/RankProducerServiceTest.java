package com.lolsearcher.reactive.unit.service.kafka.producer.rank;

import com.lolsearcher.reactive.constant.constant.KafkaConstant;
import com.lolsearcher.reactive.model.entity.rank.Rank;
import com.lolsearcher.reactive.service.kafka.rank.RankProducerService;
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
public class RankProducerServiceTest {

    @Mock private ReactiveKafkaProducerTemplate<String, Rank> template;
    private RankProducerService rankProducerService;

    @BeforeEach
    public void setup(){
        rankProducerService = new RankProducerService(template);
    }

    @DisplayName("레코드가 카프카에 정상적으로 저장되면 플로우가 성공적으로 종료된다.")
    @Test
    public void sendRecordWithSuccess(){

        //given
        Rank record = RankProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = RankProducerServiceTestSetup.getSenderResultWithSuccess();
        BDDMockito.given(template.send(KafkaConstant.RANK_TOPIC, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(rankProducerService.sendRecord(record))
                .expectComplete()
                .verify();
    }

    @DisplayName("레코드가 카프카에 저장 실패하면 예외가 발생한다.")
    @Test
    public void sendRecordWithError(){

        //given
        Rank record = RankProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = RankProducerServiceTestSetup.getSenderResultWithFail();
        BDDMockito.given(template.send(KafkaConstant.RANK_TOPIC, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(rankProducerService.sendRecord(record))
                .expectError()
                .verify();
    }
}
