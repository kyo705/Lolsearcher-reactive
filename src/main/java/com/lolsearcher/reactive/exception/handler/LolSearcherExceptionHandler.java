package com.lolsearcher.reactive.exception.handler;

import com.lolsearcher.reactive.model.output.error.ErrorResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

import static com.lolsearcher.reactive.constant.BeanNameConstants.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class LolSearcherExceptionHandler {

    private final Map<String, ResponseEntity<ErrorResponseBody>> errorResponseEntities;

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponseBody> getResponseError(WebClientResponseException e) {

        log.error("'{}' error occurred by 'Riot' game server", e.getStatusCode().value());

        if (e.getStatusCode() == HttpStatus.BAD_GATEWAY ||
                e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR ||
                e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE ||
                e.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT
        ) {
            log.error("라이엇 게임 서버에서 에러가 발생");
            log.error(e.getMessage());
            return errorResponseEntities.get(BAD_GATEWAY_ENTITY_NAME);
        }
        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND) ||
                e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {

            log.error("클라이언트의 요청에 해당하는 소환사 정보가 없음");
            return errorResponseEntities.get(NOT_FOUND_ENTITY_NAME);
        }
        if (e.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {

            log.error("너무 많은 API 요청이 들어옴");
            return errorResponseEntities.get(TOO_MANY_REQUESTS_ENTITY_NAME);
        }

        log.error("해당 서버에서 RIOT GAMES API 설정이 잘못됨");
        return errorResponseEntities.get(INTERNAL_SERVER_ERROR_ENTITY_NAME);
    }
}
