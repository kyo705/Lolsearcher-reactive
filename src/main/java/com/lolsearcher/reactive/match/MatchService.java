package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.errors.exception.IllegalRiotGamesResponseDataException;
import com.lolsearcher.reactive.match.dto.MatchDto;
import com.lolsearcher.reactive.match.dto.SummaryMemberDto;
import com.lolsearcher.reactive.match.riotgamesdto.RiotGamesTotalMatchDto;
import com.lolsearcher.reactive.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

import static com.lolsearcher.reactive.config.ReactiveKafkaProducerConfig.MQ_THREAD_PREFIX;
import static com.lolsearcher.reactive.match.MatchConstant.KR_REGION_PREFIX;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {

    private final MatchMessageQueue matchMessageQueue;
    private final MatchAPI matchAPI;

    public Flux<MatchDto> findRecentMatches(MatchRequest request) {

        TotalMatchRecordDto totalMatchRecord = initializeTotalMatchRecord(request);

        return matchAPI.findMatchIds(request.getPuuid(), request.getLastMatchId(), request.getCount(), request.getQueueId())
                .doOnNext(matchId->setLastMatchId(matchId, totalMatchRecord))
                .flatMap(matchId -> matchAPI.findMatch(matchId).onErrorResume(e -> handle429Exception(e, totalMatchRecord)))
                .map(ResponseFactory::getMatchDto)
                .doOnNext(match -> totalMatchRecord.getSuccessMatches().add(match))
                .filter(match -> checkRequestCondition(match, request))
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

    private Mono<RiotGamesTotalMatchDto> handle429Exception(Throwable e, TotalMatchRecordDto totalMatchRecord) {

        if(e instanceof WebClientResponseException){
            WebClientResponseException wcex = (WebClientResponseException) e;
            String matchId = extractMatchId(requireNonNull(wcex.getMessage()));

            if(wcex.getStatusCode() == TOO_MANY_REQUESTS){

                log.info("너무 많은 요청으로 인해 MATCH_ID : {} 요청 실패", matchId);
                log.info(wcex.getMessage());
                totalMatchRecord.getFailMatchIds().add(matchId);
                return Mono.empty();
            }
            else if(wcex.getStatusCode() == NOT_FOUND){
                log.error(e.getMessage());
                return Mono.error(new IllegalRiotGamesResponseDataException(String.format("matchId : %s is not exist", matchId)));
            }
        }
        return Mono.error(e);
    }

    private String extractMatchId(String message) {

        int s = message.indexOf(KR_REGION_PREFIX);
        int e = !message.contains("?") ? message.length() : message.indexOf("?");

        return message.substring(s, e);
    }

    private boolean checkRequestCondition(MatchDto match, MatchRequest request) {

        if(request.getChampionId() == null){
            return true;
        }
        for (SummaryMemberDto member : match.getSummaryMember()) {
            if(member.getSummonerId().equals(request.getSummonerId()) && member.getPickChampionId() == request.getChampionId()){
                return true;
            }
        }
        return false;
    }

    private void sendToMessageQueue(TotalMatchRecordDto totalMatchRecord) {

        Flux<Void> successMatchFlow = createSuccessMatchFlow(totalMatchRecord);
        Flux<Void> failMatchIdFlow = createFailMatchIdFlow(totalMatchRecord);
        Mono<Void> remainMatchIdRangeFlow = createRemainMatchIdRangeFlow(totalMatchRecord);

        //위의 플로우들을 합치고 subscribe 시도
        Flux.empty()
                .concatWith(successMatchFlow)
                .concatWith(failMatchIdFlow)
                .concatWith(remainMatchIdRangeFlow)
                .doOnComplete(() -> createLastMatchIdFlow(totalMatchRecord).subscribe()) //위의 플로우들이 정상적으로 완료된 경우 마지막으로 유저 데이터 갱신
                .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                .subscribe();
    }

    private Flux<Void> createSuccessMatchFlow(TotalMatchRecordDto totalMatchRecord) {

        if(totalMatchRecord.getSuccessMatches().isEmpty()){
            return Flux.empty();
        }
        return Flux.fromIterable(totalMatchRecord.getSuccessMatches())
                .flatMap(matchMessageQueue::sendSuccessMatch);
    }

    private Flux<Void> createFailMatchIdFlow(TotalMatchRecordDto totalMatchRecord) {

        if(totalMatchRecord.getFailMatchIds().isEmpty()){
            return Flux.empty();
        }
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
