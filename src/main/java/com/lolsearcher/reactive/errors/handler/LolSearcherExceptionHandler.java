package com.lolsearcher.reactive.errors.handler;

import com.lolsearcher.reactive.ban.BanService;
import com.lolsearcher.reactive.errors.exception.IllegalRiotGamesResponseDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import java.util.Map;

import static com.lolsearcher.reactive.config.ErrorResponseEntityConfig.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class LolSearcherExceptionHandler {

    private final Map<String, ResponseEntity<ErrorResponseBody>> errorResponseEntities;
    private final BanService banService;

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponseBody> getResponseError(WebClientResponseException e, ServerWebExchange exchange) {

        log.error("'{}' error occurred by 'Riot' game server", e.getStatusCode().value());
        log.error(e.getMessage());

        if (e.getStatusCode().equals(HttpStatus.NOT_FOUND) || e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {

            addAbusingCount(exchange);

            log.error("클라이언트의 요청에 해당하는 적절한 데이터가 없음");
            return errorResponseEntities.get(NOT_FOUND_ENTITY_NAME);
        }
        else if (e.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {

            addAbusingCount(exchange);

            log.error("너무 많은 API 요청이 들어옴");
            return errorResponseEntities.get(TOO_MANY_REQUESTS_ENTITY_NAME);
        }
        else if (
                e.getStatusCode() == HttpStatus.BAD_GATEWAY ||
                e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR ||
                e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE ||
                e.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT
        ) {
            log.error("라이엇 게임 서버에서 에러가 발생");
            log.error(e.getMessage());
            return errorResponseEntities.get(BAD_GATEWAY_ENTITY_NAME);
        }

        log.error("서버에서 RIOT GAMES API 설정이 잘못됨");
        return errorResponseEntities.get(INTERNAL_SERVER_ERROR_ENTITY_NAME);
    }

    private void addAbusingCount(ServerWebExchange exchange) {

        if(exchange.getRequest().getRemoteAddress() == null){
            log.error("ip 주소가 없음 ");
            return;
        }
        String ipAddress = exchange.getRequest().getRemoteAddress().getHostName();

        Mono.just(ipAddress)
                .flatMap(banService::addAbusingCount)
                .subscribe();
    }


    @ExceptionHandler({
            ConstraintViolationException.class,         // pathvariable @validated 검사 실패 시
            MethodArgumentNotValidException.class,      // requestBody @valid 검사 실패 시
            ConversionFailedException.class,		    // enum type data bind 실패시
            IllegalArgumentException.class,             // custom으로 파라미터 검사 실패 시
            MethodArgumentTypeMismatchException.class,
            WebExchangeBindException.class,			    // modelAttribute 바인딩 실패 시
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponseBody> handleInvalidArgumentException(Exception e) {

        log.error("잘못된 파라미터 요청 " + e.getMessage());

        return errorResponseEntities.get(BAD_REQUEST_ENTITY_NAME);
    }

    @ExceptionHandler({IllegalRiotGamesResponseDataException.class})
    public ResponseEntity<ErrorResponseBody> handleExternalServerException(Exception e) {

        log.error(e.getMessage());

        return errorResponseEntities.get(BAD_GATEWAY_ENTITY_NAME);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponseBody> anyException(Exception e) {

        log.error(e.getClass().getName());
        log.error(e.getMessage());

        return errorResponseEntities.get(INTERNAL_SERVER_ERROR_ENTITY_NAME);
    }
}
