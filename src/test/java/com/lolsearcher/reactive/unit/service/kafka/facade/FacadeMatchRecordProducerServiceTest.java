package com.lolsearcher.reactive.unit.service.kafka.facade;

import com.lolsearcher.reactive.model.input.kafka.SummonerMatchData;
import com.lolsearcher.reactive.service.kafka.FacadeMatchRecordProducerService;
import com.lolsearcher.reactive.service.kafka.RecordProducerService;
import com.lolsearcher.reactive.service.kafka.match.FailMatchIdProducerService;
import com.lolsearcher.reactive.service.kafka.match.LastMatchIdProducerService;
import com.lolsearcher.reactive.service.kafka.match.RemainMatchIdRangeProducerService;
import com.lolsearcher.reactive.service.kafka.match.SuccessMatchProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FacadeMatchRecordProducerServiceTest {

    @Mock private Map<String, RecordProducerService> recordProducerServices;
    @Mock private SuccessMatchProducerService successMatchProducerService;
    @Mock private FailMatchIdProducerService failMatchIdProducerService;
    @Mock private RemainMatchIdRangeProducerService remainMatchIdRangeProducerService;
    @Mock private LastMatchIdProducerService lastMatchIdProducerService;
    private FacadeMatchRecordProducerService facadeMatchRecordProducerService;

    @BeforeEach
    public void setup(){
        facadeMatchRecordProducerService = new FacadeMatchRecordProducerService(recordProducerServices);
    }

    @DisplayName("매치 관련 데이터들이 정상적으로 카프카에 저장되었을 경우, 유저의 lastMatchId 값을 갱신하는 레코드를 카프카에 저장한다.")
    @Test
    public void sendMatchDataWithSuccess() throws InterruptedException {

        //given
        SummonerMatchData request = FacadeServiceTestSetup.getSummonerMatchData();

        given(recordProducerServices.get(SUCCESS_MATCH_TEMPLATE)).willReturn(successMatchProducerService);
        given(recordProducerServices.get(FAIL_MATCH_ID_TEMPLATE)).willReturn(failMatchIdProducerService);
        given(recordProducerServices.get(REMAIN_MATCH_ID_RANGE_TEMPLATE)).willReturn(remainMatchIdRangeProducerService);
        given(recordProducerServices.get(LAST_MATCH_ID_TEMPLATE)).willReturn(lastMatchIdProducerService);

        given(successMatchProducerService.sendRecord(any())).willReturn(Mono.empty());
        given(failMatchIdProducerService.sendRecord(any())).willReturn(Mono.empty());
        given(remainMatchIdRangeProducerService.sendRecord(request.getPuuId(), request.getRemainMatchIdRange())).willReturn(Mono.empty());
        given(lastMatchIdProducerService.sendRecord(request.getSummonerId(), request.getLastMatchId())).willReturn(Mono.empty());

        //when
        facadeMatchRecordProducerService.send(request);
        Thread.sleep(1000);

        //then
        verify(lastMatchIdProducerService, times(1)).sendRecord(request.getSummonerId(), request.getLastMatchId());
    }

    @DisplayName("매치 관련 데이터들이 하나라도 카프카에 저장되지 못한다면, 유저의 lastMatchId 값을 갱신하는 레코드는 카프카에 저장되지 않는다.")
    @Test
    public void sendMatchDataWithError() throws InterruptedException {

        //given
        SummonerMatchData request = FacadeServiceTestSetup.getSummonerMatchData();

        given(recordProducerServices.get(SUCCESS_MATCH_TEMPLATE)).willReturn(successMatchProducerService);
        given(recordProducerServices.get(FAIL_MATCH_ID_TEMPLATE)).willReturn(failMatchIdProducerService);
        given(recordProducerServices.get(REMAIN_MATCH_ID_RANGE_TEMPLATE)).willReturn(remainMatchIdRangeProducerService);

        given(successMatchProducerService.sendRecord(any())).willReturn(Mono.empty());
        given(failMatchIdProducerService.sendRecord(any())).willReturn(Mono.error(new RuntimeException()));
        given(remainMatchIdRangeProducerService.sendRecord(request.getPuuId(), request.getRemainMatchIdRange())).willReturn(Mono.empty());

        //when
        facadeMatchRecordProducerService.send(request);
        Thread.sleep(1000);

        //then
        verify(lastMatchIdProducerService, times(0)).sendRecord(any(), any());
    }
}
