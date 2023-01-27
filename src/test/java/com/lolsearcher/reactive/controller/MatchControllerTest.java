package com.lolsearcher.reactive.controller;

import com.lolsearcher.reactive.config.redis.ReactiveRedisConfig;
import com.lolsearcher.reactive.exception.handler.LolSearcherExceptionHandler;
import com.lolsearcher.reactive.model.input.front.RequestMatchDto;
import com.lolsearcher.reactive.model.output.error.ErrorResponseBody;
import com.lolsearcher.reactive.model.output.error.ErrorResponseEntity;
import com.lolsearcher.reactive.model.output.match.MatchDto;
import com.lolsearcher.reactive.service.ban.BanService;
import com.lolsearcher.reactive.service.match.MatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@Import({BanService.class, ReactiveRedisConfig.class, MatchController.class,
        LolSearcherExceptionHandler.class, ErrorResponseEntity.class})
@WebFluxTest(useDefaultFilters = false)
public class MatchControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @MockBean
    MatchService matchService;

    @DisplayName("정상적인 서비스가 응답될 경우 200 ok를 리턴한다.")
    @Test
    void getMatchesDtoWithSuccess(){

        //given
        RequestMatchDto request = MatchControllerTestSetup.getRequestDto();
        Flux<MatchDto> matchDtoFlux = MatchControllerTestSetup.getMatchDtoFlux();
        given(matchService.getRecentMatchDto(request)).willReturn(matchDtoFlux);

        //when & then
        webTestClient.post()
                .uri("/summoner/match/renew")
                .accept(MediaType.APPLICATION_NDJSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MatchDto.class)
                .value(matchDto -> assertThat(matchDto.getVersion()).isEqualTo("2"));
    }

    @DisplayName("서비스에서 예외가 발생할 경우 해당 예외에 적절한 상태코드 및 메세지를 리턴한다.")
    @Test
    void getMatchesDtoWithWebClientException(){

        //given
        RequestMatchDto request = MatchControllerTestSetup.getRequestDto();
        given(matchService.getRecentMatchDto(request)).willThrow(MatchControllerTestSetup.get404Error());

        //when & then
        webTestClient.post()
                .uri("/summoner/match/renew")
                .accept(MediaType.ALL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                .expectBody(ErrorResponseBody.class)
                .value(body -> assertThat(body.getErrorMessage())
                        .isEqualTo("요청한 파라미터에 대한 정보 없음")
                );
    }
}
