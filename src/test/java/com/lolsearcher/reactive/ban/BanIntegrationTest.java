package com.lolsearcher.reactive.ban;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static com.lolsearcher.reactive.ban.BanConstant.SEARCH_BAN_COUNT;
import static com.lolsearcher.reactive.ban.BanService.getKey;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_BY_NAME_URI;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@Import({EmbeddedRedisConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class BanIntegrationTest {

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
    void beforeEach() {

        redisTemplate.scan().flatMap(redisTemplate::delete).subscribe();
    }

    @DisplayName("클라이언트가 어뷰저로 등록된 경우 403 상태코드를 리턴한다.")
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
    @ValueSource(ints = {400, 404, 429})
    @ParameterizedTest
    public void testFindByNameWithAbusingRequest(int statusCode) throws InterruptedException {

        //given
        String name = "푸켓푸켓";
        String ip = "123.123.123.123";

        assertThat(redisTemplate.opsForValue().get(getKey(ip)).block()).isNull();

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
                .expectStatus().is4xxClientError();

        Thread.sleep(1000);

        assertThat(redisTemplate.opsForValue().get(getKey(ip)).block()).isEqualTo(1);
    }
}
