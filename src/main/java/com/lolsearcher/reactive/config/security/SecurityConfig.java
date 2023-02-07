package com.lolsearcher.reactive.config.security;

import com.lolsearcher.reactive.constant.LolSearcherConstants;
import com.lolsearcher.reactive.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer(){
        return new ForwardedHeaderTransformer();
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                  CorsConfigurationSource corsConfigurationSource,
                                                  ReactiveRedisTemplate<String, Object> redisTemplate){

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(redisTemplate);

        return http.csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .pathMatchers("/**")
                .permitAll()
                .and()
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
                LolSearcherConstants.FORWARDED_HTTP_HEADER
        ));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return configurationSource;
    }
}
