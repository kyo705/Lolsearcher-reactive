package com.lolsearcher.reactive.authentication;

import reactor.core.publisher.Mono;

public interface CertificationApi {

    Mono<String> validateToken(String token);
}
