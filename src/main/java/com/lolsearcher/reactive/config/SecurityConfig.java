package com.lolsearcher.reactive.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

import java.util.List;

import static com.google.common.net.HttpHeaders.X_FORWARDED_FOR;

@RequiredArgsConstructor
@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String FRONT_SERVER_URI = "localhost:80";

    @Order(-100)
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer(){
        return new ForwardedHeaderTransformer();
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, CorsConfigurationSource corsConfigurationSource){

        return http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource).and()
                .formLogin().disable()
                .httpBasic().disable()
                .anonymous().and()
                .authorizeExchange()
                .pathMatchers("/**").permitAll().and()
                .build();
    }

    @Bean
    CorsConfigurationSource configurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.CONTENT_LANGUAGE,
                HttpHeaders.HOST,
                HttpHeaders.CONNECTION,
                HttpHeaders.ACCEPT,
                HttpHeaders.ACCEPT_ENCODING,
                HttpHeaders.USER_AGENT,
                X_FORWARDED_FOR
        ));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST"));
        corsConfiguration.setAllowedOrigins(List.of(FRONT_SERVER_URI));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return configurationSource;
    }
}
