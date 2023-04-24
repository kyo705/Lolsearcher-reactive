package com.lolsearcher.reactive.api.certification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.constant.constant.UriConstants.CERTIFICATION_SERVER_TOKEN_VALIDATION_URI;

@RequiredArgsConstructor
@Component
public class WebClientCertificationApi implements CertificationApi {

    private final WebClient certificationWebClient;

    @Override
    public Mono<String> validateToken(String token) {

        return certificationWebClient
                .get()
                .uri(CERTIFICATION_SERVER_TOKEN_VALIDATION_URI, token)
                .retrieve()
                .bodyToMono(String.class);
    }
}
