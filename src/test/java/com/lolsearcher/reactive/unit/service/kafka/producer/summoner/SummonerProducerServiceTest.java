package com.lolsearcher.reactive.unit.service.kafka.producer.summoner;

import com.lolsearcher.reactive.model.entity.summoner.Summoner;
import com.lolsearcher.reactive.service.kafka.summoner.SummonerProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;
import reactor.test.StepVerifier;

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.SUMMONER_TOPIC;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SummonerProducerServiceTest {

    @Mock private ReactiveKafkaProducerTemplate<String, Summoner> template;
    private SummonerProducerService summonerProducerService;

    @BeforeEach
    public void setup(){
        summonerProducerService = new SummonerProducerService(template);
    }

    @DisplayName("레코드가 카프카에 정상적으로 저장되면 플로우가 성공적으로 종료된다.")
    @Test
    public void sendRecordWithSuccess(){

        //given
        Summoner record = SummonerProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = SummonerProducerServiceTestSetup.getSenderResultWithSuccess();
        given(template.send(SUMMONER_TOPIC, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(summonerProducerService.sendRecord(record))
                .expectComplete()
                .verify();
    }

    @DisplayName("레코드가 카프카에 저장 실패하면 예외가 발생한다.")
    @Test
    public void sendRecordWithError(){

        //given
        Summoner record = SummonerProducerServiceTestSetup.getRecord();

        SenderResult<Void> senderResult = SummonerProducerServiceTestSetup.getSenderResultWithFail();
        given(template.send(SUMMONER_TOPIC, record)).willReturn(Mono.just(senderResult));

        //when & then
        StepVerifier.create(summonerProducerService.sendRecord(record))
                .expectError()
                .verify();
    }
}
