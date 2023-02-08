package com.lolsearcher.reactive.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Configuration
public class lolsearcherConfig {

    private final WebClient.Builder webclientBuilder;

    @Value("${lolsearcher.webclient.kr-base-url}")
    private String krBaseUrl;

    @Value("${lolsearcher.webclient.asia-base-url}")
    private String asiaBaseUrl;

    @Qualifier("koreaWebClient")
    @Bean
    public WebClient koreaWebClient() {
        return webclientBuilder
                .baseUrl(krBaseUrl)
                .build();
    }

    @Qualifier("asiaWebClient")
    @Bean
    public WebClient asiaWebClient() {
        return webclientBuilder
                .baseUrl(asiaBaseUrl)
                .build();
    }
}
