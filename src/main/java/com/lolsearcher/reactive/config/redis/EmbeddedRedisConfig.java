package com.lolsearcher.reactive.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Configuration
public class EmbeddedRedisConfig {


    private final RedisServer redisServer = new RedisServer(17058);

    @PostConstruct
    public void postConstruct() {
        log.info("Embedded Redis 서버 연결");
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        log.info("Embedded Redis 서버 종료");
        redisServer.stop();
    }
}