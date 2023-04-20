package com.lolsearcher.reactive.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lolsearcher.reactive.model.security.JwtAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.lolsearcher.reactive.constant.constant.JWTConstant.*;
import static com.lolsearcher.reactive.constant.constant.UriConstants.JWT_RECREATION_URI;

@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${lolsearcher.jwt.secret}")
    private String secret = "tempKey";

    private final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        return Mono.justOrEmpty(token)
                .doOnNext(this::isValidToken)
                .flatMap(this::setSecurityContextHolder)
                .doOnNext(v -> chain.filter(exchange))
                .switchIfEmpty(chain.filter(exchange))
                .onErrorResume(TokenExpiredException.class, (e) -> handleExpiredToken(exchange))
                .onErrorResume(Exception.class, e -> chain.filter(exchange));
    }

    private Mono<Void> handleExpiredToken(ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create(JWT_RECREATION_URI));

        return Mono.empty();
    }

    private void isValidToken(String token) {

        DecodedJWT decodedJWT = verifier.verify(token);

        if(!decodedJWT.getSubject().equals(CERTIFICATED_SERVER)){
            throw new IllegalArgumentException("허용되지 않은 토큰 입니다.");
        }
    }

    private Mono<Void> setSecurityContextHolder(String token) {

        String user = JWT.decode(token).getClaim(CERTIFICATED_SERVER_USERID).asString();
        String authority = JWT.decode(token).getClaim(CERTIFICATED_SERVER_AUTHORITY).asString();

        Authentication authentication = new JwtAuthentication(user, authority);

        ReactiveSecurityContextHolder.withAuthentication(authentication);

        return Mono.empty();
    }
}
