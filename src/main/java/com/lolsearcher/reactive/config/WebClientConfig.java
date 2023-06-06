package com.lolsearcher.reactive.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Configuration
public class WebClientConfig {

    public static final String KR_WEB_CLIENT_NAME = "koreaWebClient";
    public static final String ASIA_WEB_CLIENT_NAME = "asiaWebClient";
    private final WebClient.Builder webclientBuilder;

    @Value("${lolsearcher.webclient.kr-base-url}")
    private String krBaseUrl;

    @Value("${lolsearcher.webclient.asia-base-url}")
    private String asiaBaseUrl;

    @Qualifier(KR_WEB_CLIENT_NAME)
    @Bean
    public WebClient koreaWebClient() {
        return webclientBuilder
                .baseUrl(krBaseUrl)
                .build();
    }

    @Qualifier(ASIA_WEB_CLIENT_NAME)
    @Bean
    public WebClient asiaWebClient() {
        return webclientBuilder
                .baseUrl(asiaBaseUrl)
                .build();
    }
}
