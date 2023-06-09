package com.lolsearcher.reactive.rank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.lolsearcher.reactive.config.EmbeddedRedisConfig;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.Map;

import static com.lolsearcher.reactive.rank.RankConstant.RANK_URI;
import static com.lolsearcher.reactive.rank.RankConstant.THE_NUMBER_OF_RANK_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka(partitions = 3,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092"
        },
        ports = { 9092 })
@AutoConfigureWebTestClient
@Import({EmbeddedRedisConfig.class})
@ActiveProfiles("test")
@SpringBootTest
public class RankIntegrationTest {

    private static MockWebServer mockWebServer;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired private ReactiveRedisTemplate<String, Object> redisTemplate;
    @Autowired private ObjectMapper objectMapper;

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
    void beforeEach() {

        redisTemplate.scan().flatMap(redisTemplate::delete).subscribe();
    }

    @DisplayName("유효한 파라미터로 요청시 200 상태코드를 리턴한다.")
    @Test
    public void testFindAllWithValidParam() throws InterruptedException {

        //given
        String summonerId = "summonerId1";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .addHeader("Content-Type", "application/json")
                        .setBody(RankSetup.getValidRanks())
        );

        //when
        webTestClient.get()
                .uri(RANK_URI, summonerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .value(
                        map -> {
                            assertThat(map.size()).isLessThanOrEqualTo(THE_NUMBER_OF_RANK_TYPE);

                            map.values().forEach(obj -> {
                                RankDto rank = objectMapper.convertValue(obj, RankDto.class);
                                assertThat(rank.getSummonerId()).isEqualTo(summonerId);
                            });
                        });

        Thread.sleep(1000);
    }

    @DisplayName("잘못된 파라미터로 요청시 400 상태코드를 리턴한다.")
    @ValueSource(strings = {"  ", "1234567890123456789012345678901234567890123456789012345678901234"/* 64글자 */})
    @ParameterizedTest
    public void testFindAllWithInvalidParam(String summonerId) {

        //given
        String ip = "123.123.123.123";

        //when
        webTestClient.get()
                .uri(RANK_URI, summonerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @DisplayName("존재하지 않는 id로 요청시 404 상태코드를 리턴한다.")
    @Test
    public void testFindAllWithNotExistingSummonerId() {

        //given
        String summonerId = "summonerId1";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.BAD_REQUEST.value())
                        .addHeader("Content-Type", "application/json")
        );

        //when
        webTestClient.get()
                .uri(RANK_URI, summonerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("외부 서버 요청 제한 횟수 초과 시 429 상태코드를 리턴한다.")
    @Test
    public void testFindAllWithTooManyRequest() {

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
                .uri(RANK_URI, summonerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @DisplayName("외부 서버 접속 권한이 없을 경우 500 상태코드를 리턴한다.")
    @ValueSource(ints = {401, 403})
    @ParameterizedTest
    public void testFindAllWithInternalServerError(int statusCode) {

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
                .uri(RANK_URI, summonerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("외부 서버에서 가져온 데이터가 잘못된 경우 502 상태코드를 리턴한다.")
    @MethodSource("com.lolsearcher.reactive.rank.RankSetup#invalidRanks")
    @ParameterizedTest
    public void testFindAllWithBadGateway(String response) {

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
                .uri(RANK_URI, summonerId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_GATEWAY);
    }
}
