package com.lolsearcher.reactive.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearcher.reactive.config.EmbeddedRedisConfig;
import com.lolsearcher.reactive.config.MockWebServerConfig;
import com.lolsearcher.reactive.summoner.SummonerSetup;
import com.lolsearcher.reactive.summoner.WebClientSummonerAPI;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.IOException;


@ActiveProfiles("test")
@SpringBootTest(classes = {EmbeddedRedisConfig.class, MockWebServerConfig.class})
public class ReactiveCacheTest {

    public MockWebServer mockBackEnd;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;
    @Autowired
    private WebClientSummonerAPI WebClientSummonerAPI;

    @BeforeEach
    void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(8900);
    }

    @AfterEach
    void tearDown() throws InterruptedException, IOException {
        mockBackEnd.shutdown();

        reactiveStringRedisTemplate.scan()
                .flatMap(key -> reactiveStringRedisTemplate.delete(key))
                .subscribe();
    }

    @DisplayName("Cache에 존재하지 않는 데이터에 대한 요청이 동시에 여러 개 발생할 경우 하나의 요청만 비지니스 로직에 접근하고 다른 로직은 Cache에서 데이터를 가져온다.")
    @Test
    public void testConcurrency() throws IOException {

        //given
        String summonerDto = SummonerSetup.getRiotGamesSummonerDto();

        mockBackEnd.enqueue(new MockResponse()
                .setBody(summonerDto)
                .addHeader("Content-Type", "application/json"));

        //when & then
        StepVerifier.create(
                        Flux.concat(WebClientSummonerAPI.findByName("test_key"),
                                        WebClientSummonerAPI.findByName("test_key"),
                                        WebClientSummonerAPI.findByName("test_key")
                                )
                                .parallel()
                                .runOn(Schedulers.parallel())
                                .sequential()
                )
                .assertNext(dto -> Assertions.assertThat(mockBackEnd.getRequestCount()).isEqualTo(1))
                .assertNext(dto -> Assertions.assertThat(mockBackEnd.getRequestCount()).isEqualTo(1))
                .assertNext(dto -> Assertions.assertThat(mockBackEnd.getRequestCount()).isEqualTo(1))
                .verifyComplete();
    }

    @DisplayName("캐시에 저장된 값이 TTL 시간이 지나면 삭제가 된다.")
    @Test
    public void testTTL() throws InterruptedException {

        //given
        String summonerDto = SummonerSetup.getRiotGamesSummonerDto();

        mockBackEnd.enqueue(new MockResponse()
                .setBody(summonerDto)
                .addHeader("Content-Type", "application/json"));

        mockBackEnd.enqueue(new MockResponse()
                .setBody(summonerDto)
                .addHeader("Content-Type", "application/json"));

        //캐시에 데이터가 존재하지 않기 때문에 open api 요청함
        StepVerifier.create(WebClientSummonerAPI.findByName("test_key"))
                .assertNext(dto -> Assertions.assertThat(mockBackEnd.getRequestCount()).isEqualTo(1))
                .verifyComplete();

        //캐시에 데이터가 존재하기 때문에 캐시로부터 데이터 가져옴
        Thread.sleep(1000);
        StepVerifier.create(WebClientSummonerAPI.findByName("test_key"))
                .assertNext(dto -> Assertions.assertThat(mockBackEnd.getRequestCount()).isEqualTo(1))
                .verifyComplete();

        //when & then => ttl 시간(2초)이 경과한 후 다시 요청 application-test.yml에 설정값 저장
        Thread.sleep(2000);
        StepVerifier.create(WebClientSummonerAPI.findByName("test_key"))
                .assertNext(dto -> Assertions.assertThat(mockBackEnd.getRequestCount()).isEqualTo(2))
                .verifyComplete();
    }
}
