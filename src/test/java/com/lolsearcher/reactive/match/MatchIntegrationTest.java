package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.config.EmbeddedRedisConfig;
import com.lolsearcher.reactive.config.GeneralSetup;
import com.lolsearcher.reactive.match.dto.MatchDto;
import com.lolsearcher.reactive.match.dto.SummaryMemberDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.net.HttpHeaders.X_FORWARDED_FOR;
import static com.lolsearcher.reactive.config.ErrorResponseEntityConfig.*;
import static com.lolsearcher.reactive.match.MatchConstant.MATCH_URI;
import static com.lolsearcher.reactive.match.MatchSetup.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_NDJSON;


@EmbeddedKafka(partitions = 3,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092"
        },
        ports = { 9092 })
@AutoConfigureWebTestClient
@Import({EmbeddedRedisConfig.class})
@ActiveProfiles("test")
@SpringBootTest
public class MatchIntegrationTest extends GeneralSetup {

    private static MockWebServer mockWebServer;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired private ReactiveRedisTemplate<String, Object> redisTemplate;
    @Autowired private Map<String, ResponseEntity<ErrorResponseBody>> errorResponseEntities;

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


    @DisplayName("유효한 파라미터로 요청시 200 상태코드를 리턴한다.")
    @Test
    public void testFindMatchesWithValidParam() throws IOException, InterruptedException {

        //given
        String summonerId = "Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getValidMatchIds())
        );
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getValidMatch())
        );

        //when & then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(MATCH_URI)
                        .queryParam("puuid", "puuid1")
                        .queryParam("queueId", 420)
                        .build(summonerId))
                .accept(APPLICATION_NDJSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MatchDto.class)
                .value(matches -> matches.forEach(
                        match -> assertThat(match.getSummaryMember().stream().map(SummaryMemberDto::getSummonerId).collect(Collectors.toList()))
                                .contains(summonerId)
                ));

        Thread.sleep(1000);
    }

    @DisplayName("잘못된 파라미터로 요청시 400 상태코드를 리턴한다.")
    @MethodSource("com.lolsearcher.reactive.match.MatchSetup#invalidRequest")
    @ParameterizedTest
    public void testFindMatchesWithInvalidParam(String puuId, int count, int queueId, int championId) {

        //given
        String summonerId = "Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A";
        String ip = "123.123.123.123";

        //when & then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(MATCH_URI)
                        .queryParam("puuid", puuId)
                        .queryParam("count", count)
                        .queryParam("queueId", queueId)
                        .queryParam("championId", championId)
                        .build(summonerId))
                .accept(APPLICATION_NDJSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponseBody.class).value(body -> {
                    ErrorResponseBody expected = errorResponseEntities.get(BAD_REQUEST_ENTITY_NAME).getBody();

                    assertThat(expected).isNotNull();
                    assertThat(body.getErrorMessage()).isEqualTo(expected.getErrorMessage());
                });

    }

    @DisplayName("존재하지 않는 puuId일 경우 404 상태코드를 리턴한다.")
    @Test
    public void testFindMatchesWithNotExistingSummonerId() {

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
                .uri(uriBuilder -> uriBuilder
                        .path(MATCH_URI)
                        .queryParam("puuid", "NOT_EXISTING_PUU_ID")
                        .queryParam("queueId", 420)
                        .build(summonerId))
                .accept(APPLICATION_NDJSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponseBody.class).value(body -> {
                    ErrorResponseBody expected = errorResponseEntities.get(NOT_FOUND_ENTITY_NAME).getBody();

                    assertThat(expected).isNotNull();
                    assertThat(body.getErrorMessage()).isEqualTo(expected.getErrorMessage());
                });
    }

    @DisplayName("Open API로부터 얻은 matchId가 존재하지 않을 경우 502 상태코드를 리턴한다.")
    @Test
    public void testFindMatchesWithNotExistingMatchId() {

        //given
        String summonerId = "Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getValidMatchIds())
        );
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.NOT_FOUND.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        //when & then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(MATCH_URI)
                        .queryParam("puuid", "puuid1")
                        .queryParam("queueId", 420)
                        .build(summonerId))
                .accept(APPLICATION_NDJSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_GATEWAY)
                .expectBody(ErrorResponseBody.class).value(body -> {
                    ErrorResponseBody expected = errorResponseEntities.get(BAD_GATEWAY_ENTITY_NAME).getBody();

                    assertThat(expected).isNotNull();
                    assertThat(body.getErrorMessage()).isEqualTo(expected.getErrorMessage());
                });
    }

    @DisplayName("OPEN API로부터 얻은 데이터가 유효하지 않을 경우 502 상태코드를 리턴한다.")
    @Test
    public void testFindMatchesWithInvalidOpenAPIData() throws IOException {

        //given
        String summonerId = "Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getValidMatchIds())
        );
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getInvalidMatch())
        );

        //when & then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(MATCH_URI)
                        .queryParam("puuid", "puuid1")
                        .queryParam("queueId", 420)
                        .build(summonerId))
                .accept(APPLICATION_NDJSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_GATEWAY)
                .expectBody(ErrorResponseBody.class).value(body -> {
                    ErrorResponseBody expected = errorResponseEntities.get(BAD_GATEWAY_ENTITY_NAME).getBody();

                    assertThat(expected).isNotNull();
                    assertThat(body.getErrorMessage()).isEqualTo(expected.getErrorMessage());
                });
    }

    @DisplayName("MatchId 데이터 조회 중 요청횟수 초과시 429 상태코드를 리턴한다.")
    @Test
    public void testFindMatchesWithTooManyRequestsFromMatchId() {

        //given
        String summonerId = "Q3bZKpY5NUVblppL1CRZ0v19FctWQz19QmRq6Sqpd3pDUxiSCj2-EvoP3A";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.TOO_MANY_REQUESTS.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getValidMatchIds())
        );

        //when & then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(MATCH_URI)
                        .queryParam("puuid", "puuid1")
                        .queryParam("queueId", 420)
                        .build(summonerId))
                .accept(APPLICATION_NDJSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS)
                .expectBody(ErrorResponseBody.class).value(body -> {
                    ErrorResponseBody expected = errorResponseEntities.get(TOO_MANY_REQUESTS_ENTITY_NAME).getBody();

                    assertThat(expected).isNotNull();
                    assertThat(body.getErrorMessage()).isEqualTo(expected.getErrorMessage());
                });
    }


    @DisplayName("Match 데이터 조회 중 요청횟수 초과시 실패한 요청 정보를 카프카로 전달한다.")
    @Test
    public void testFindMatchesWithTooManyRequestsFromMatch() throws InterruptedException {

        //given
        String summonerId = "NOT_EXISTING_SUMMONER_ID";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getValidMatchIds())
        );
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.TOO_MANY_REQUESTS.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        //when & then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(MATCH_URI)
                        .queryParam("puuid", "puuid1")
                        .queryParam("queueId", 420)
                        .build(summonerId))
                .accept(APPLICATION_NDJSON)
                .header(X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MatchDto.class).value(list -> assertThat(list.size()).isEqualTo(0));

        Thread.sleep(1000);
    }

}
