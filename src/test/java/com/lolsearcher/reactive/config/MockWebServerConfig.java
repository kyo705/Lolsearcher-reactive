package com.lolsearcher.reactive.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class MockWebServerConfig {

    @Qualifier(WebClientConfig.KR_WEB_CLIENT_NAME)
    @Bean
    public WebClient webClient() {

        return WebClient.builder().baseUrl("http://localhost:8900").build();
    }
}
