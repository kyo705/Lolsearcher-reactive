package com.lolsearcher.reactive.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearcher.reactive.config.EmbeddedRedisConfig;
import com.lolsearcher.reactive.config.MockWebServerConfig;
import com.lolsearcher.reactive.summoner.RiotGamesSummonerDto;
import com.lolsearcher.reactive.summoner.SummonerSetup;
import com.lolsearcher.reactive.summoner.WebClientSummonerAPI;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.IOException;


@ActiveProfiles("test")
@SpringBootTest(classes = {EmbeddedRedisConfig.class, MockWebServerConfig.class})
public class ReactiveCacheTest {

    public static MockWebServer mockBackEnd;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebClientSummonerAPI WebClientSummonerAPI;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(8900);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
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
                ).expectNext(objectMapper.readValue(summonerDto, RiotGamesSummonerDto.class))
                .expectNext(objectMapper.readValue(summonerDto, RiotGamesSummonerDto.class))
                .assertNext(dto -> {
                    Assertions.assertThat(mockBackEnd.getRequestCount()).isEqualTo(1);
                }).verifyComplete();
    }
}
