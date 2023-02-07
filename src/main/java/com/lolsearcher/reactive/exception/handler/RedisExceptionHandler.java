package com.lolsearcher.reactive.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearcher.reactive.exception.RedisSaveException;
import com.lolsearcher.reactive.model.output.error.ErrorResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.lolsearcher.reactive.constant.BeanNameConstants.INTERNAL_SERVER_ERROR_ENTITY_NAME;

@Order(-3)
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;
    private final Map<String, ResponseEntity<ErrorResponseBody>> errorResponseEntities;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        if(!(ex instanceof RedisSaveException)){
            return Mono.error(ex);
        }

        log.info("{} 에러 발생", ex.getMessage());

        ResponseEntity<ErrorResponseBody> responseEntity = errorResponseEntities.get(INTERNAL_SERVER_ERROR_ENTITY_NAME);

        exchange.getResponse().setStatusCode(responseEntity.getStatusCode());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_NDJSON);

        try {
            byte[] body = objectMapper.writeValueAsBytes(responseEntity.getBody());

            DefaultDataBuffer dataBuffer = DefaultDataBufferFactory.sharedInstance.allocateBuffer();
            dataBuffer.write(body);

            return exchange.getResponse().writeWith(Mono.just(dataBuffer));

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
