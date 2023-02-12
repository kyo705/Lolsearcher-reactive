package com.lolsearcher.reactive.service.match;

import com.lolsearcher.reactive.api.RiotGamesApi;
import com.lolsearcher.reactive.constant.GameType;
import com.lolsearcher.reactive.model.input.front.RequestMatchDto;
import com.lolsearcher.reactive.service.kafka.KafkaMessageProducerService;
import com.lolsearcher.reactive.service.search.match.MatchService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private RiotGamesApi riotGamesApi;
    @Mock
    private KafkaMessageProducerService kafkaProducerService;

    private MatchService matchService;

    @BeforeEach
    public void setup(){
        matchService = new MatchService(kafkaProducerService, riotGamesApi);
    }

    @DisplayName("REST API 요청에 대한 응답이 모두 성공할 경우 정상적인 플로우가 흐른다.")
    @Test
    public void getRecentMatchTestWithSuccess(){

        //given
        RequestMatchDto request = new RequestMatchDto();

        List<String> matchIds = List.of("matchId1", "matchId2", "matchId3");
        given(riotGamesApi.getMatchIds(request.getPuuid(), request.getLastMatchId(), request.getMatchCount()))
                .willReturn(Flux.fromIterable(matchIds));

        matchIds.forEach(matchId -> given(riotGamesApi.getMatches(matchId))
                .willReturn(Mono.just(MatchServiceTestSetUp.getApiMatchDto(matchId))));

        //when & then
        StepVerifier.create(matchService.getRecentMatchDto(request))
                .consumeNextWith(matchDto ->
                        Assertions.assertThat(matchDto.getQueueId()).isEqualTo(GameType.SOLO_RANK_MODE.getQueueId()))
                .consumeNextWith(matchDto ->
                        Assertions.assertThat(matchDto.getQueueId()).isEqualTo(GameType.FLEX_RANK_MODE.getQueueId()))
                .consumeNextWith(matchDto ->
                        Assertions.assertThat(matchDto.getQueueId()).isEqualTo(GameType.NORMAL_MODE.getQueueId())).expectComplete()
                .verify();

    }

    @DisplayName("REST API 요청에 대해 http 429 에러가 발생할 경우 해당 요청은 무시하고 나머지 정상적인 플로우가 흐른다.")
    @Test
    public void getRecentMatchTestWithWebClientException(){

        //given
        RequestMatchDto request = new RequestMatchDto();

        List<String> matchIds = List.of("matchId1", "matchId2", "matchId3");
        given(riotGamesApi.getMatchIds(request.getPuuid(), request.getLastMatchId(), request.getMatchCount()))
                .willReturn(Flux.fromIterable(matchIds));

        for(int i=0;i<matchIds.size();i++){
            String matchId = matchIds.get(i);
            if(i==1){
                given(riotGamesApi.getMatches(matchId))
                        .willReturn(Mono.error(new WebClientResponseException(
                                HttpStatus.TOO_MANY_REQUESTS.value(),
                                HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(),
                                null, null, null))
                        );
            }else{
                given(riotGamesApi.getMatches(matchId))
                        .willReturn(Mono.just(MatchServiceTestSetUp.getApiMatchDto(matchId)));
            }
        }

        //when & then
        StepVerifier.create(matchService.getRecentMatchDto(request))
                .consumeNextWith(matchDto ->
                    Assertions.assertThat(matchDto.getQueueId()).isEqualTo(GameType.SOLO_RANK_MODE.getQueueId()))
                .consumeNextWith(matchDto ->
                        Assertions.assertThat(matchDto.getQueueId()).isEqualTo(GameType.NORMAL_MODE.getQueueId()))
                .verifyComplete();

    }

    @DisplayName("REST API 요청에 대해 http 429 에러 이외의 에러가 발생할 경우 예외를 리턴한다.")
    @Test
    public void getRecentMatchTestWithAnotherWebClientException(){

        //given
        RequestMatchDto request = new RequestMatchDto();

        List<String> matchIds = List.of("matchId1", "matchId2", "matchId3");
        given(riotGamesApi.getMatchIds(request.getPuuid(), request.getLastMatchId(), request.getMatchCount()))
                .willReturn(Flux.fromIterable(matchIds));

        for(int i=0;i<matchIds.size();i++){
            String matchId = matchIds.get(i);
            if(i==2){
                given(riotGamesApi.getMatches(matchId))
                        .willReturn(Mono.error(new WebClientResponseException(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                null, null, null))
                        );
            }else{
                given(riotGamesApi.getMatches(matchId))
                        .willReturn(Mono.just(MatchServiceTestSetUp.getApiMatchDto(matchId)));
            }
        }

        //when & then
        StepVerifier.create(matchService.getRecentMatchDto(request))
                .consumeNextWith(matchDto ->
                        Assertions.assertThat(matchDto.getQueueId()).isEqualTo(GameType.SOLO_RANK_MODE.getQueueId()))
                .consumeNextWith(matchDto ->
                        Assertions.assertThat(matchDto.getQueueId()).isEqualTo(GameType.FLEX_RANK_MODE.getQueueId()))
                .consumeErrorWith(e ->
                        Assertions.assertThat(e).isInstanceOf(RuntimeException.class))
                .verify();

    }
}
