package com.lolsearcher.reactive.ingame;

import com.lolsearcher.reactive.config.EmbeddedRedisConfig;
import com.lolsearcher.reactive.config.GeneralSetup;
import com.lolsearcher.reactive.ingame.dto.InGameDto;
import com.lolsearcher.reactive.ingame.dto.InGameParticipantDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.net.HttpHeaders.X_FORWARDED_FOR;
import static com.lolsearcher.reactive.ingame.InGameConstant.INGAME_URI;
import static com.lolsearcher.reactive.ingame.InGameSetup.getValidRiotGamesInGameDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AutoConfigureWebTestClient
@Import({EmbeddedRedisConfig.class})
@ActiveProfiles("test")
@SpringBootTest
public class InGameIntegrationTest extends GeneralSetup {

    private static MockWebServer mockWebServer;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired private ReactiveRedisTemplate<String, Object> redisTemplate;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(15678);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void beforeEach() throws InterruptedException {

        redisTemplate.scan()
                .flatMap(redisTemplate::delete)
                .collectList()
                .flatMap(obj -> redisTemplate.opsForValue().multiSet(setupCache()))
                .subscribe();

        Thread.sleep(500);
    }

    @DisplayName("summoner가 현재 게임 진행 중인 경우 진행 중인 게임 데이터를 리턴한다.")
    @Test
    public void testFindByIdWithValidParam() {

        //given
        String summonerId = "Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getValidRiotGamesInGameDto())
        );

        //when & then
        webTestClient.get()
                .uri(INGAME_URI, summonerId)
                .accept(APPLICATION_JSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isOk()
                .expectBody(InGameDto.class).value(inGame -> {
                    List<String> summonerIds = inGame.getParticipants()
                            .stream()
                            .map(InGameParticipantDto::getSummonerId)
                            .collect(Collectors.toList());

                    assertThat(summonerIds).contains(summonerId);
                });
    }

    @DisplayName("존재하지 않는 summonerId가 전달될 경우 404 상태코드를 리턴한다.")
    @ValueSource(strings = {" ", "1234567890123456789012345678901234567890123456789012345678901234" /*64 자리 */})
    @ParameterizedTest
    public void testFindByIdWithInvalidParam(String summonerId) {

        //given
        String ip = "123.123.123.123";

        //when & then
        webTestClient.get()
                .uri(INGAME_URI, summonerId)
                .accept(APPLICATION_JSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @DisplayName("존재하지 않는 summonerId가 전달될 경우 404 상태코드를 리턴한다.")
    @Test
    public void testFindByIdWithNotExistingSummonerId() {

        //given
        String summonerId = "Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.BAD_REQUEST.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        //when & then
        webTestClient.get()
                .uri(INGAME_URI, summonerId)
                .accept(APPLICATION_JSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("조회하려는 유저가 현재 게임 진행 중이지 않을 경우 null을 리턴한다.")
    @Test
    public void testFindByIdWithNotExistingInGameData() {

        //given
        String summonerId = "Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.NOT_FOUND.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        //when & then
        webTestClient.get()
                .uri(INGAME_URI, summonerId)
                .accept(APPLICATION_JSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isOk()
                .expectBody(InGameDto.class).value(inGame -> assertThat(inGame).isNull());
    }


    @DisplayName("외부 서버 요청 제한 횟수 초과 시 429 상태코드를 리턴한다.")
    @Test
    public void testFindByIdWithTooManyRequest() {

        //given
        String summonerId = "summonerId1";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.TOO_MANY_REQUESTS.value())
                        .addHeader("Content-Type", "application/json")
        );

        //when
        webTestClient.get()
                .uri(INGAME_URI, summonerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @DisplayName("외부 서버 접속 권한이 없을 경우 500 상태코드를 리턴한다.")
    @ValueSource(ints = {401, 403})
    @ParameterizedTest
    public void testFindByIdWithInternalServerError(int statusCode) {

        //given
        String summonerId = "summonerId1";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(statusCode)
                        .addHeader("Content-Type", "application/json")
        );

        //when
        webTestClient.get()
                .uri(INGAME_URI, summonerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("외부 서버에서 가져온 데이터가 잘못된 경우 502 상태코드를 리턴한다.")
    @MethodSource("com.lolsearcher.reactive.ingame.InGameSetup#getInvalidRiotGamesInGameDto")
    @ParameterizedTest
    public void testFindByIdWithBadGateway(String response) {

        //given
        String summonerId = "summonerId1";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .addHeader("Content-Type", "application/json")
                        .setBody(response)
        );

        //when
        webTestClient.get()
                .uri(INGAME_URI, summonerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_GATEWAY);
    }
}
