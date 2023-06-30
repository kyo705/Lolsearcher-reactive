package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.errors.exception.IllegalRiotGamesResponseDataException;
import com.lolsearcher.reactive.match.dto.MatchDto;
import com.lolsearcher.reactive.match.dto.SummaryMemberDto;
import com.lolsearcher.reactive.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import static com.lolsearcher.reactive.match.MatchConstant.KR_REGION_PREFIX;
import static com.lolsearcher.reactive.match.MatchConstant.MATCH_DEFAULT_COUNT;
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
                .take(MATCH_DEFAULT_COUNT)
                .doOnNext(matchId->setLastMatchId(matchId, totalMatchRecord))
                .flatMap(matchAPI::findMatch)
                .onErrorContinue(this::handle429Exception)
                .map(ResponseFactory::getMatchDto)
                .doOnNext(matchMessageQueue::sendSuccessMatch)
                .filter(match -> checkRequestCondition(match, request))
                .doOnComplete(() -> {
                    matchMessageQueue.sendRemainMatchIds(totalMatchRecord.getPuuId(), totalMatchRecord.getRemainMatchIdRange());
                    matchMessageQueue.sendLastMatchId(totalMatchRecord.getSummonerId(), totalMatchRecord.getLastMatchId());
                });
    }

    private TotalMatchRecordDto initializeTotalMatchRecord(MatchRequest request) {

        String puuId = request.getPuuid();
        String summonerId = request.getSummonerId();
        String summonerLastMatchId = request.getLastMatchId();

        RemainMatchIdRange remainMatchIdRange = new RemainMatchIdRange();
        remainMatchIdRange.setEndRemainMatchId(summonerLastMatchId);

        TotalMatchRecordDto totalMatchRecord = new TotalMatchRecordDto();
        totalMatchRecord.setPuuId(puuId);
        totalMatchRecord.setSummonerId(summonerId);
        totalMatchRecord.setRemainMatchIdRange(remainMatchIdRange);

        return totalMatchRecord;
    }

    private void setLastMatchId(String matchId, TotalMatchRecordDto totalMatchRecord){

        if(totalMatchRecord.getLastMatchId() == null){ //최신 matchId를 유저의 lastMatchId로 설정
            totalMatchRecord.setLastMatchId(matchId);
        }
        RemainMatchIdRange remainMatchIdRange = totalMatchRecord.getRemainMatchIdRange();
        remainMatchIdRange.setStartRemainMatchId(matchId);
    }

    private void handle429Exception(Throwable e, Object obj) {

        if(e instanceof WebClientResponseException){
            WebClientResponseException wcex = (WebClientResponseException) e;
            String matchId = extractMatchId(requireNonNull(wcex.getMessage()));

            if(wcex.getStatusCode() == TOO_MANY_REQUESTS){

                log.info("너무 많은 요청으로 인해 MATCH_ID : {} 요청 실패", matchId);
                log.info(wcex.getMessage());
                matchMessageQueue.sendFailMatchId(matchId);
                return;
            }
            else if(wcex.getStatusCode() == NOT_FOUND){
                log.error(e.getMessage());
                throw  new IllegalRiotGamesResponseDataException(String.format("matchId : %s is not exist", matchId));
            }
        }
        log.error(e.getMessage());
        throw (RuntimeException) e;
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
}
