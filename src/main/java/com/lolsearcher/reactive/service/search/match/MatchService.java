package com.lolsearcher.reactive.service.search.match;

import com.lolsearcher.reactive.api.riot.RiotGamesApi;
import com.lolsearcher.reactive.constant.enumeration.GameType;
import com.lolsearcher.reactive.constant.constant.LolSearcherConstants;
import com.lolsearcher.reactive.exception.exception.IncorrectDataVersionException;
import com.lolsearcher.reactive.model.entity.match.Match;
import com.lolsearcher.reactive.model.entity.match.SummaryMember;
import com.lolsearcher.reactive.model.entity.match.Team;
import com.lolsearcher.reactive.model.factory.EntityFactory;
import com.lolsearcher.reactive.model.factory.ResponseFactory;
import com.lolsearcher.reactive.model.input.front.RequestMatchDto;
import com.lolsearcher.reactive.model.input.kafka.RemainMatchIdRange;
import com.lolsearcher.reactive.model.input.kafka.SummonerMatchData;
import com.lolsearcher.reactive.model.input.riotgames.match.RiotGamesTotalMatchDto;
import com.lolsearcher.reactive.model.output.match.MatchDto;
import com.lolsearcher.reactive.service.kafka.FacadeMatchRecordProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {

    private final FacadeMatchRecordProducerService facadeRecordProducerService;
    private final RiotGamesApi riotGamesApi;

    public Flux<MatchDto> getRecentMatchDto(RequestMatchDto request) {

        SummonerMatchData summonerMatchData = initializeSummonerMatchData(request);

        return riotGamesApi.getMatchIds(request.getPuuid(), request.getLastMatchId(), request.getMatchCount())
                .doOnNext(matchId->setLastMatchId(matchId, summonerMatchData))
                .flatMap(riotGamesApi::getMatches)
                .onErrorContinue((e, matchId) -> handle429Exception(e, matchId, summonerMatchData))
                .map(this::changeRequestMatchDtoToEntity)
                .doOnNext(match -> summonerMatchData.getSuccessMatches().add(match))
                .map(match -> getResponseMatchDto(match, request))
                .doOnComplete(() -> facadeRecordProducerService.send(summonerMatchData));
    }

    private SummonerMatchData initializeSummonerMatchData(RequestMatchDto request) {

        String puuId = request.getPuuid();
        String summonerId = request.getSummonerId();
        String summonerLastMatchId = request.getLastMatchId();

        List<Match> successMatches = new ArrayList<>();
        List<String> failMatchIds = new ArrayList<>();
        RemainMatchIdRange remainMatchIdRange = new RemainMatchIdRange();
        remainMatchIdRange.setEndRemainMatchId(summonerLastMatchId);

        SummonerMatchData summonerMatchData = new SummonerMatchData();
        summonerMatchData.setPuuId(puuId);
        summonerMatchData.setSuccessMatches(successMatches);
        summonerMatchData.setFailMatchIds(failMatchIds);
        summonerMatchData.setSummonerId(summonerId);
        summonerMatchData.setRemainMatchIdRange(remainMatchIdRange);

        return summonerMatchData;
    }

    private void setLastMatchId(String matchId, SummonerMatchData summonerMatchData){

        if(summonerMatchData.getLastMatchId() == null){
            summonerMatchData.setLastMatchId(matchId);
        }
        RemainMatchIdRange remainMatchIdRange = summonerMatchData.getRemainMatchIdRange();
        remainMatchIdRange.setStartRemainMatchId(matchId);
    }

    private void handle429Exception(Throwable e, Object matchId, SummonerMatchData summonerMatchData) {

        if(e instanceof WebClientResponseException){
            if(((WebClientResponseException) e).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS){
                log.info("너무 많은 요청으로 인해 MATCH_ID : {} 요청 실패", matchId);

                summonerMatchData.getFailMatchIds().add((String) matchId);
                return;
            }
        }
        log.error(e.getMessage());
        throw new RuntimeException(e);
    }

    private Match changeRequestMatchDtoToEntity(RiotGamesTotalMatchDto riotGamesMatchDto) {

        String requestDataVersion = riotGamesMatchDto.getMetadata().getDataVersion();
        //받아온 데이터 버전 확인
        if (!requestDataVersion.equals(LolSearcherConstants.MATCH_DATA_VERSION)) {
            throw new IncorrectDataVersionException(requestDataVersion);
        }
        try {
            return EntityFactory.getMatchFromApiDto(riotGamesMatchDto);
        } catch (IllegalAccessException e) {
            log.error("API 데이터를 Entity 객체로 변환할 수 없음.");
            throw new RuntimeException(e);
        }
    }

    private MatchDto getResponseMatchDto(Match match, RequestMatchDto request) {

        if(isCorrespondWithCondition(match, request)){
            return ResponseFactory.getResponseMatchDtoFromEntity(match);
        }
        return null;
    }

    private boolean isCorrespondWithCondition(Match successMatch, RequestMatchDto request) {

        String summonerId = request.getSummonerId();
        int queueId = request.getQueueId();
        int championId = request.getChampionId();

        if(successMatch.getQueueId() != queueId && queueId != GameType.ALL_QUEUE_ID.getQueueId()){
            return false;
        }
        if(championId == -1){
            return true;
        }

        for(Team team : successMatch.getTeams()){
            for(SummaryMember member : team.getMembers()){
                if(!member.getSummonerId().equals(summonerId)){
                    continue;
                }
                if(member.getPickChampionId() == championId){
                    return true;
                }
            }
        }
        return false;
    }
}
