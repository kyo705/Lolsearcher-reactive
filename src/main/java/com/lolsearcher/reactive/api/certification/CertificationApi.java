package com.lolsearcher.reactive.api.certification;

import reactor.core.publisher.Mono;

public interface CertificationApi {

    Mono<String> validateToken(String token);
}
