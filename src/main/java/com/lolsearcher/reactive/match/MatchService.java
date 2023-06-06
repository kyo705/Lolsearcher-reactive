package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.match.dto.MatchDto;
import com.lolsearcher.reactive.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {

    private final MatchMessageQueue matchMessageQueue;
    private final MatchAPI matchAPI;

    public Flux<MatchDto> findRecentMatches(MatchRequest request) {

        TotalMatchRecordDto totalMatchRecord = initializeTotalMatchRecord(request);

        return matchAPI.findMatchIds(request.getPuuid(), request.getLastMatchId(), request.getMatchCount())
                .doOnNext(matchId->setLastMatchId(matchId, totalMatchRecord))
                .flatMap(matchAPI::findMatch)
                .onErrorContinue((e, matchId) -> handle429Exception(e, matchId, totalMatchRecord))
                .map(ResponseFactory::getMatchDto)
                .doOnNext(match -> totalMatchRecord.getSuccessMatches().add(match))
                .doOnComplete(() -> sendToMessageQueue(totalMatchRecord));
    }

    private TotalMatchRecordDto initializeTotalMatchRecord(MatchRequest request) {

        String puuId = request.getPuuid();
        String summonerId = request.getSummonerId();
        String summonerLastMatchId = request.getLastMatchId();

        List<MatchDto> successMatches = new ArrayList<>();
        List<String> failMatchIds = new ArrayList<>();

        RemainMatchIdRange remainMatchIdRange = new RemainMatchIdRange();
        remainMatchIdRange.setEndRemainMatchId(summonerLastMatchId);

        TotalMatchRecordDto totalMatchRecord = new TotalMatchRecordDto();
        totalMatchRecord.setPuuId(puuId);
        totalMatchRecord.setSuccessMatches(successMatches);
        totalMatchRecord.setFailMatchIds(failMatchIds);
        totalMatchRecord.setSummonerId(summonerId);
        totalMatchRecord.setRemainMatchIdRange(remainMatchIdRange);

        return totalMatchRecord;
    }

    private void setLastMatchId(String matchId, TotalMatchRecordDto totalMatchRecord){

        if(totalMatchRecord.getLastMatchId() == null){
            totalMatchRecord.setLastMatchId(matchId);
        }
        RemainMatchIdRange remainMatchIdRange = totalMatchRecord.getRemainMatchIdRange();
        remainMatchIdRange.setStartRemainMatchId(matchId);
    }

    private void handle429Exception(Throwable e, Object matchId, TotalMatchRecordDto totalMatchRecord) {

        if(e instanceof WebClientResponseException){
            if(((WebClientResponseException) e).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS){
                log.info("너무 많은 요청으로 인해 MATCH_ID : {} 요청 실패", matchId);

                totalMatchRecord.getFailMatchIds().add((String) matchId);
                return;
            }
        }
        log.error(e.getMessage());
        throw new RuntimeException(e);
    }

    public void sendToMessageQueue(TotalMatchRecordDto totalMatchRecord) {

        Flux<Void> successMatchFlow = createSuccessMatchFlow(totalMatchRecord);
        Flux<Void> failMatchIdFlow = createFailMatchIdFlow(totalMatchRecord);
        Mono<Void> remainMatchIdRangeFlow = createRemainMatchIdRangeFlow(totalMatchRecord);

        //위의 플로우들을 합치고 subscribe 시도
        successMatchFlow
                .concatWith(failMatchIdFlow)
                .concatWith(remainMatchIdRangeFlow)
                .doOnComplete(() -> createLastMatchIdFlow(totalMatchRecord).subscribe()) //위의 플로우들이 정상적으로 완료된 경우 마지막으로 유저 데이터 갱신
                .subscribe();
    }

    private Flux<Void> createSuccessMatchFlow(TotalMatchRecordDto totalMatchRecord) {

        return Flux.fromIterable(totalMatchRecord.getSuccessMatches())
                .flatMap(matchMessageQueue::sendSuccessMatch);
    }

    private Flux<Void> createFailMatchIdFlow(TotalMatchRecordDto totalMatchRecord) {

        return Flux.fromIterable(totalMatchRecord.getFailMatchIds())
                .flatMap(matchMessageQueue::sendFailMatchId);
    }

    private Mono<Void> createRemainMatchIdRangeFlow(TotalMatchRecordDto totalMatchRecord) {

        String puuId = totalMatchRecord.getPuuId();
        RemainMatchIdRange remainMatchIdRange = totalMatchRecord.getRemainMatchIdRange();

        return matchMessageQueue.sendRemainMatchIds(puuId, remainMatchIdRange);
    }

    private Mono<Void> createLastMatchIdFlow(TotalMatchRecordDto totalMatchRecord) {

        String summonerId = totalMatchRecord.getSummonerId();
        String lastMatchId = totalMatchRecord.getLastMatchId();

        return matchMessageQueue.sendLastMatchId(summonerId, lastMatchId);
    }
}
