package com.lolsearcher.reactive.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearcher.reactive.api.certification.CertificationApi;
import com.lolsearcher.reactive.constant.enumeration.CertificationStatus;
import com.lolsearcher.reactive.model.output.error.ErrorResponseBody;
import com.lolsearcher.reactive.model.security.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.lolsearcher.reactive.constant.constant.JWTConstant.CERTIFICATED_SERVER_AUTHORITY;
import static com.lolsearcher.reactive.constant.constant.JWTConstant.CERTIFICATED_SERVER_USERID;
import static com.lolsearcher.reactive.constant.constant.UriConstants.JWT_RECREATION_URI;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private final CertificationApi certificationApi;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 인증 서버에 해당 토큰이 유효한지 확인하는 api 호출
        // => 응답으로 200이 올 경우에만 ReactiveSecurityContextHolder에 authentication 객체 주입
        // => 응답으로 만료된 토큰일 경우에만 인증 서버로 리다이렉트, 나머지는 그냥 필터 체인 타도록 허용 -> anonymous 필터로 이후 처리

        return Mono.justOrEmpty(token)
                .flatMap(this::validateToken)
                .flatMap(jwt -> chain.filter(exchange))
                .switchIfEmpty(chain.filter(exchange))
                .onErrorResume(WebClientResponseException.class,
                        e -> handleWebClientResponseException(e, exchange, chain));
    }

    private Mono<String> validateToken(String token) {

        return certificationApi.validateToken(token)
                .doOnNext(this::setSecurityContextHolder);
    }

    private void setSecurityContextHolder(String token) {

        String user = JWT.decode(token).getClaim(CERTIFICATED_SERVER_USERID).asString();
        String authority = JWT.decode(token).getClaim(CERTIFICATED_SERVER_AUTHORITY).asString();

        Authentication authentication = new JwtAuthentication(user, authority);

        ReactiveSecurityContextHolder.withAuthentication(authentication);
    }

    private Mono<Void> handleWebClientResponseException(
            WebClientResponseException e, ServerWebExchange exchange, WebFilterChain chain
    ) {
        try {
            ErrorResponseBody body =  objectMapper.readValue(e.getMessage(), ErrorResponseBody.class);

            if(body.getErrorStatusCode() == CertificationStatus.EXPIRED.getStatusCode()){
                ServerHttpResponse response = exchange.getResponse();

                response.setStatusCode(HttpStatus.FOUND);
                response.getHeaders().setLocation(URI.create(JWT_RECREATION_URI));

                return Mono.empty();
            }
            else{
                log.debug(e.getMessage());
                return chain.filter(exchange);
            }
        } catch (JsonProcessingException ex) {
            return Mono.error(ex);
        }
    }
}
