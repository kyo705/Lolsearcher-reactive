package com.lolsearcher.reactive.summoner;

import com.google.common.net.HttpHeaders;
import com.lolsearcher.reactive.config.EmbeddedRedisConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static com.lolsearcher.reactive.ban.BanConstant.SEARCH_BAN_COUNT;
import static com.lolsearcher.reactive.ban.BanService.getKey;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_BY_NAME_URI;

@EmbeddedKafka(partitions = 3,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092"
        },
        ports = { 9092 })
@AutoConfigureWebTestClient
@Import({EmbeddedRedisConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class SummonerIntegrationTest {

    private static MockWebServer mockWebServer;
    @Autowired private WebTestClient webTestClient;
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
    void beforeEach() {

        redisTemplate.scan().flatMap(redisTemplate::delete).subscribe();
    }

    @DisplayName("정상적인 요청일 경우 200 상태코드를 리턴한다.")
    @Test
    public void testRenewByNameWithValidParam() throws InterruptedException {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .addHeader("Content-Type", "application/json")
                        .setBody(SummonerSetup.getRiotGamesSummonerDto())
        );

        //when & then
        webTestClient.get()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SummonerDto.class).value(match -> Assertions.assertThat(match.getSummonerName()).isEqualTo(name));

        Thread.sleep(1000);
    }

    @DisplayName("잘못된 파라미터로 요청할 경우 400 상태코드를 리턴한다.")
    @ValueSource(strings = {"  ", "123456789012345678901234567890123456789012345678901" /* 51자 */})
    @ParameterizedTest
    public void testRenewByNameWithInvalidParam(String name) {

        //given
        String ip = "123.123.123.123";

        //when & then
        webTestClient.get()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isBadRequest();
    }


    @DisplayName("닉네임에 해당하는 데이터가 없을 경우 404 상태코드를 리턴한다.")
    @Test
    public void testRenewByNameWithNotFound() {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.NOT_FOUND.value())
                        .addHeader("Content-Type", "application/json")
        );

        //when & then
        webTestClient.get()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("외부 서버 접속 권한이 없을 경우 500 상태코드를 리턴한다.")
    @ValueSource(ints = {401, 403})
    @ParameterizedTest
    public void testRenewByNameWithExternalServerNonAuthorizedError(int statusCode) {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(statusCode)
                        .addHeader("Content-Type", "application/json")
        );

        //when & then
        webTestClient.get()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("클라이언트가 어뷰저로 등록된 경우 403 상태코드를 리턴한다.")
    @Test
    public void testRenewByNameWithNonAuthorizedError() {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        redisTemplate.opsForValue().set(getKey(ip), SEARCH_BAN_COUNT).subscribe();

        //when & then
        webTestClient.get()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }


}
