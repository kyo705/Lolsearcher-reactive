package com.lolsearcher.reactive.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearcher.reactive.constant.BeanNameConstants;
import com.lolsearcher.reactive.model.output.error.ErrorResponseBody;
import com.lolsearcher.reactive.service.ban.BanService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class SearchBanFilter implements WebFilter {

    private final ObjectMapper objectMapper;
    private final BanService banService;
    private final Map<String, ResponseEntity<ErrorResponseBody>> errorResponseEntities;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return Mono.just(exchange)
                .flatMap(banService::inspect)
                .flatMap(invalidUser->getUnAuthorizedException(exchange))
                .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<Void> getUnAuthorizedException(ServerWebExchange exchange) {

        ResponseEntity<ErrorResponseBody> responseEntity = errorResponseEntities.get(BeanNameConstants.FORBIDDEN_ENTITY_NAME);

        exchange.getResponse().setStatusCode(responseEntity.getStatusCode());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_NDJSON);

        try {
            byte[] body = objectMapper.writeValueAsBytes(responseEntity.getBody());

            DefaultDataBuffer dataBuffer = DefaultDataBufferFactory.sharedInstance.allocateBuffer();
            dataBuffer.write(body);

            return exchange.getResponse().writeWith(Mono.just(dataBuffer));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
