package com.lolsearcher.reactive.service.kafka;

import com.lolsearcher.reactive.model.input.kafka.RemainMatchIdRange;
import com.lolsearcher.reactive.model.input.kafka.SummonerMatchData;
import com.lolsearcher.reactive.service.kafka.match.FailMatchIdProducerService;
import com.lolsearcher.reactive.service.kafka.match.LastMatchIdProducerService;
import com.lolsearcher.reactive.service.kafka.match.RemainMatchIdRangeProducerService;
import com.lolsearcher.reactive.service.kafka.match.SuccessMatchProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.*;
import static com.lolsearcher.reactive.constant.constant.KafkaConstant.LAST_MATCH_ID_TEMPLATE;

@Slf4j
@RequiredArgsConstructor
@Service
public class FacadeMatchRecordProducerService {

    private final Map<String, RecordProducerService> recordProducerServices;


    public void send(SummonerMatchData summonerMatchData) {

        Flux<Void> successMatchFlow = createSuccessMatchFlow(summonerMatchData);
        Flux<Void> failMatchIdFlow = createFailMatchIdFlow(summonerMatchData);
        Mono<Void> remainMatchIdRangeFlow = createRemainMatchIdRangeFlow(summonerMatchData);

        //위의 플로우들을 합치고 subscribe 시도
        successMatchFlow
                .concatWith(failMatchIdFlow)
                .concatWith(remainMatchIdRangeFlow)
                .doOnComplete(() -> createLastMatchIdFlow(summonerMatchData).subscribe()) //위의 플로우들이 정상적으로 완료된 경우 마지막으로 유저 데이터 갱신
                .subscribe();
    }

    private Flux<Void> createSuccessMatchFlow(SummonerMatchData summonerMatchData) {

        SuccessMatchProducerService successMatchProducerService =
                (SuccessMatchProducerService) recordProducerServices.get(SUCCESS_MATCH_TEMPLATE);

        return Flux.fromIterable(summonerMatchData.getSuccessMatches())
                .flatMap(successMatchProducerService::sendRecord);
    }

    private Flux<Void> createFailMatchIdFlow(SummonerMatchData summonerMatchData) {

        FailMatchIdProducerService failMatchIdProducerService =
                (FailMatchIdProducerService) recordProducerServices.get(FAIL_MATCH_ID_TEMPLATE);

        return Flux.fromIterable(summonerMatchData.getFailMatchIds())
                .flatMap(failMatchIdProducerService::sendRecord);
    }

    private Mono<Void> createRemainMatchIdRangeFlow(SummonerMatchData summonerMatchData) {

        String puuId = summonerMatchData.getPuuId();
        RemainMatchIdRange remainMatchIdRange = summonerMatchData.getRemainMatchIdRange();

        RemainMatchIdRangeProducerService remainMatchIdRangeProducerService =
                (RemainMatchIdRangeProducerService) recordProducerServices.get(REMAIN_MATCH_ID_RANGE_TEMPLATE);

        return remainMatchIdRangeProducerService.sendRecord(puuId, remainMatchIdRange);
    }

    private Mono<Void> createLastMatchIdFlow(SummonerMatchData summonerMatchData) {

        String summonerId = summonerMatchData.getSummonerId();
        String lastMatchId = summonerMatchData.getLastMatchId();

        return ((LastMatchIdProducerService)recordProducerServices.get(LAST_MATCH_ID_TEMPLATE))
                .sendRecord(summonerId, lastMatchId);
    }
}
