package com.lolsearcher.reactive.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.lolsearcher.reactive.exception.NonAuthorizedException;
import com.lolsearcher.reactive.exception.RedisSaveException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.lolsearcher.reactive.constant.LolSearcherConstants.RENEW_PATH;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${lolsearcher.jwt.secret}")
    private String secret;

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
    private JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return Mono.just(exchange)
                .filter(this::checkURI)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(path -> checkJwtToken(exchange))
                .flatMap(token -> successHandler(token, exchange, chain));
    }

    private boolean checkURI(ServerWebExchange exchange){

        String uriPath = exchange.getRequest().getURI().getPath();

        if(uriPath.contains(RENEW_PATH)){
            return true;
        }
        return false;
    }

    private Mono<String> checkJwtToken(ServerWebExchange exchange) {

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        return Mono.justOrEmpty(token)
                .doOnNext(jwtToken -> verifier.verify(jwtToken))
                .onErrorResume(Exception.class, e -> Mono.empty())
                .switchIfEmpty(Mono.error(new NonAuthorizedException("토큰이 없거나 만료되었습니다.")))
                .flatMap(jwtToken -> reactiveRedisTemplate.opsForValue().get(jwtToken))
                .flatMap(result -> Mono.error(new NonAuthorizedException("해당 토큰은 이미 한 번 사용되었습니다.")))
                .switchIfEmpty(Mono.just(token))
                .flatMap(jwtToken -> Mono.just(token));
    }

    private Mono<Void> successHandler(String token, ServerWebExchange exchange, WebFilterChain chain) {

        return Mono.just(token)
                .flatMap(this::removeAuthorization)
                .flatMap(result->chain.filter(exchange));
    }

    private Mono<Boolean> removeAuthorization(String token){

        return reactiveRedisTemplate.opsForValue().set(token, true, Duration.ofMinutes(1))
                .filter(isSuccess -> isSuccess)
                .switchIfEmpty(Mono.error(new RedisSaveException("Redis에 저장 실패")));
    }

}
