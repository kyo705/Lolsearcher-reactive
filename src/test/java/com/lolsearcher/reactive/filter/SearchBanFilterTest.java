package com.lolsearcher.reactive.filter;

import com.lolsearcher.reactive.config.security.SecurityConfig;
import com.lolsearcher.reactive.controller.InGameController;
import com.lolsearcher.reactive.exception.handler.LolSearcherExceptionHandler;
import com.lolsearcher.reactive.exception.handler.AuthenticationExceptionHandler;
import com.lolsearcher.reactive.model.input.front.RequestInGameDto;
import com.lolsearcher.reactive.model.output.error.ErrorResponseBody;
import com.lolsearcher.reactive.model.output.error.ErrorResponseEntity;
import com.lolsearcher.reactive.model.output.ingame.InGameDto;
import com.lolsearcher.reactive.service.ban.BanService;
import com.lolsearcher.reactive.service.ingame.InGameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@Import({SearchBanFilter.class, BanService.class, InGameController.class, SecurityConfig.class,
        AuthenticationExceptionHandler.class ,LolSearcherExceptionHandler.class, ErrorResponseEntity.class})
@WebFluxTest(value = InGameController.class, useDefaultFilters = false)
public class SearchBanFilterTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private InGameService inGameService;
    @MockBean
    private ReactiveRedisTemplate<String, Object> template;
    @MockBean
    private ReactiveValueOperations<String, Object> operations;

    @DisplayName("request의 ip주소가 없을 경우 403 에러를 발생시킨다.")
    @Test
    void searchBanFilterTestWithNoExistingIpAddress(){

        //given
        RequestInGameDto request = new RequestInGameDto("summonerId");

        //when & then
        webTestClient.post()
                .uri("/summoner/ingame")
                .accept(MediaType.APPLICATION_NDJSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorResponseBody.class)
                .value(body -> assertThat(body.getErrorMessage())
                        .isEqualTo("해당 클라이언트 접근 권한 없음")
                );
    }

    @DisplayName("어뷰징 횟수가 특정 횟수를 초과하지 않은 클라이언트가 요청시 다음 필터로 넘어간다.")
    @Test
    void searchBanFilterTestWithNotExceedingAbusingCount() {

        //given
        RequestInGameDto request = new RequestInGameDto("summonerId");
        InGameDto result = SearchBanFilterTestSetup.getInGameDto();
        given(inGameService.getInGame(request)).willReturn(Mono.just(result));

        given(template.opsForValue()).willReturn(operations);
        given(operations.get(any())).willReturn(Mono.just(1));

        //when & then
        webTestClient.post()
                .uri("/summoner/ingame")
                .header("X-Forwarded-For", "1.1.1.1")
                .accept(MediaType.APPLICATION_NDJSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(InGameDto.class)
                .value(inGameDto -> assertThat(inGameDto.getGameId()).isEqualTo(result.getGameId()));
    }

    @DisplayName("어뷰징 횟수가 특정 횟수를 초과한 클라이언트가 요청시 403 에러를 발생시킨다.")
    @Test
    void searchBanFilterTestWithExceedingAbusingCount() {

        //given
        RequestInGameDto request = new RequestInGameDto("summonerId");

        given(template.opsForValue()).willReturn(operations);
        given(operations.get(any())).willReturn(Mono.just(30));

        //when & then
        webTestClient.post()
                .uri("/summoner/ingame")
                .header("X-Forwarded-For", "1.1.1.1")
                .accept(MediaType.APPLICATION_NDJSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorResponseBody.class)
                .value(body -> assertThat(body.getErrorMessage())
                        .isEqualTo("해당 클라이언트 접근 권한 없음")
                );
    }
}
