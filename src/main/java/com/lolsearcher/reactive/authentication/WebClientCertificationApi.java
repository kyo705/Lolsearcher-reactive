package com.lolsearcher.reactive.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.authentication.AuthenticationConstant.CERTIFICATION_SERVER_TOKEN_VALIDATION_URI;

@RequiredArgsConstructor
@Component
public class WebClientCertificationApi implements CertificationApi {

    private final WebClient asiaWebClient;

    @Override
    public Mono<String> validateToken(String token) {

        return asiaWebClient
                .get()
                .uri(CERTIFICATION_SERVER_TOKEN_VALIDATION_URI, token)
                .retrieve()
                .bodyToMono(String.class);
    }
}
