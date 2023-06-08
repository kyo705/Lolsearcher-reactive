package com.lolsearcher.reactive.summoner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.lolsearcher.reactive.config.EmbeddedRedisConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
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
import java.util.List;

import static com.lolsearcher.reactive.ban.BanConstant.SEARCH_BAN_COUNT;
import static com.lolsearcher.reactive.ban.BanService.getKey;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_BY_NAME_URI;
import static com.lolsearcher.reactive.summoner.SummonerSetup.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("Find : 정상적인 요청일 경우 200 상태코드를 리턴한다.")
    @Test
    public void testFindByNameWithValidParam() throws InterruptedException {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .addHeader("Content-Type", "application/json")
                        .setBody(getRiotGamesSummonerDto())
        );

        //when & then
        webTestClient.get()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SummonerDto.class).value(match -> assertThat(match.getSummonerName()).isEqualTo(name));

        Thread.sleep(1000);
    }

    @DisplayName("Find : 잘못된 파라미터로 요청할 경우 400 상태코드를 리턴한다.")
    @ValueSource(strings = {"  ", "123456789012345678901234567890123456789012345678901" /* 51자 */})
    @ParameterizedTest
    public void testFindByNameWithInvalidParam(String name) {

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


    @DisplayName("Find : 닉네임에 해당하는 데이터가 없을 경우 404 상태코드를 리턴한다.")
    @Test
    public void testFindByNameWithNotFound() {

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

    @DisplayName("Find : 허용된 요청 횟수 초과시 429 상태코드를 리턴한다.")
    @Test
    public void testFindByNameWithTooManyRequest() {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.TOO_MANY_REQUESTS.value())
                        .addHeader("Content-Type", "application/json")
        );

        //when & then
        webTestClient.get()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @DisplayName("Find : 외부 서버 접속 권한이 없을 경우 500 상태코드를 리턴한다.")
    @ValueSource(ints = {401, 403})
    @ParameterizedTest
    public void testFindByNameWithExternalServerNonAuthorizedError(int statusCode) {

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

    @DisplayName("Find : 클라이언트가 어뷰저로 등록된 경우 403 상태코드를 리턴한다.")
    @Test
    public void testFindByNameWithNonAuthorizedError() {

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

    @DisplayName("어뷰징 요청으로 의심될 경우 해당 클라이언트 ip의 어뷰징 카운트를 1 증가 시킨다.")
    @Test
    public void testFindByNameWithAbusingRequest() throws InterruptedException {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        assertThat(redisTemplate.opsForValue().get(getKey(ip)).block()).isNull();

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.TOO_MANY_REQUESTS.value())
                        .addHeader("Content-Type", "application/json")
        );

        //when & then
        webTestClient.get()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS);

        Thread.sleep(1000);

        assertThat(redisTemplate.opsForValue().get(getKey(ip)).block()).isEqualTo(1);
    }

    @DisplayName("Update : 정상적인 요청일 경우 200 상태코드를 리턴한다.")
    @Test
    public void testUpdateWithValidParam() throws InterruptedException {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        getRiotGamesSummoners()
                .forEach(body -> mockWebServer.enqueue(
                        new MockResponse()
                                .setResponseCode(HttpStatus.OK.value())
                                .addHeader("Content-Type", "application/json")
                                .setBody(body)
                ));


        //when & then
        webTestClient.put()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validSummonerUpdateRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class).value(list -> {
                    assertThat(list.size()).isLessThanOrEqualTo(1);
                    SummonerDto summoner = objectMapper.convertValue(list.get(0), SummonerDto.class);
                    assertThat(summoner.getSummonerName()).isEqualTo(name);
                });

        Thread.sleep(1000);  // 비동기로 kafka에 produce 하기 때문에 메세지 확인 차 스레드 sleep
    }

    @DisplayName("Update : 요청한 닉네임에 알맞는 유저가 없을 경우 null을 리턴한다.")
    @Test
    public void testUpdateWithIncorrectNameSummoners() throws InterruptedException {

        //given
        String name = "이런 닉네임 없을 껄";
        String ip = "123.123.123.123";

        getRiotGamesSummoners()
                .forEach(body -> mockWebServer.enqueue(
                        new MockResponse()
                                .setResponseCode(HttpStatus.OK.value())
                                .addHeader("Content-Type", "application/json")
                                .setBody(body)
                ));


        //when & then
        webTestClient.put()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validSummonerUpdateRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class).value(list -> assertThat(list.size()).isEqualTo(0));

        Thread.sleep(1000); // 비동기로 kafka에 produce 하기 때문에 메세지 확인 차 스레드 sleep
    }

    @DisplayName("Update : 존재하지 않는 데이터로 요청할 경우 404 상태코드를 리턴한다.")
    @Test
    public void testUpdateWithInvalidParam() {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        getRiotGamesSummoners()
                .forEach(body -> mockWebServer.enqueue(
                        new MockResponse()
                                .setResponseCode(HttpStatus.BAD_REQUEST.value())
                                .addHeader("Content-Type", "application/json")
                                .setBody(body)
                ));


        //when & then
        webTestClient.put()
                .uri(SUMMONER_BY_NAME_URI, name)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.X_FORWARDED_FOR, ip)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validSummonerUpdateRequest())
                .exchange()
                .expectStatus().isNotFound();
    }
}
