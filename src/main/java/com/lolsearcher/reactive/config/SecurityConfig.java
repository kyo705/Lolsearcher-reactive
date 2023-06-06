package com.lolsearcher.reactive.config;

import com.lolsearcher.reactive.authentication.CertificationApi;
import com.lolsearcher.reactive.authentication.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String FRONT_SERVER_URI = "localhost:80";
    public static final String FORWARDED_HTTP_HEADER = "X-Forwarded-For";

    private final CertificationApi certificationApi;

    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer(){
        return new ForwardedHeaderTransformer();
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                  CorsConfigurationSource corsConfigurationSource,
                                                  ReactiveRedisTemplate<String, Object> redisTemplate){

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(certificationApi);

        return http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource).and()
                .formLogin().disable()
                .httpBasic().disable()
                .anonymous().and()
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
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
                FORWARDED_HTTP_HEADER
        ));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST"));
        corsConfiguration.setAllowedOrigins(List.of(FRONT_SERVER_URI));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return configurationSource;
    }
}
