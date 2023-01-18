package com.lolsearcher.reactive.cache;

import com.lolsearcher.reactive.api.RiotGamesApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@SpringBootTest
public class ReactiveRedisCacheableAnnotationTest {

    @Autowired
    private RiotGamesApi riotGamesApi;
    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @DisplayName("MatchId에 해당하는 Match 데이터가 캐시에 없다면 REST API로 요청 후 캐시에 저장한다.")
    @Test
    public void savingCacheTest() {

        //given
        String matchId = "KR_6294134117";

        /* 캐시에 존재하지 않음 */
        StepVerifier.create(reactiveRedisTemplate.opsForValue().get(matchId))
                .expectNextCount(0)
                .expectComplete()
                .verify();

        //when
        StepVerifier.create(riotGamesApi.getMatches(matchId))
                .expectNextCount(1)
                .expectComplete()
                .verify();

        //then
        /* 캐시에 존재함 */
        StepVerifier.create(reactiveRedisTemplate.opsForValue().get(matchId))
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @DisplayName("캐시에 저장된 데이터는 ttl 이후 삭제된다.")
    @Test
    public void timeOutCacheTest() throws InterruptedException {

        //given
        String matchId = "KR_6294134117";

        /* 캐시에 존재하지 않음 */
        StepVerifier.create(reactiveRedisTemplate.opsForValue().get(matchId))
                .expectNextCount(0)
                .expectComplete()
                .verify();

        StepVerifier.create(riotGamesApi.getMatches(matchId))
                .expectNextCount(1)
                .expectComplete()
                .verify();

        /* 캐시에 존재함 */
        StepVerifier.create(reactiveRedisTemplate.opsForValue().get(matchId))
                .expectNextCount(1)
                .expectComplete()
                .verify();

        //when
        Thread.sleep(2000);

        //then
        /* 캐시에 존재하지 않음 */
        StepVerifier.create(reactiveRedisTemplate.opsForValue().get(matchId))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}
