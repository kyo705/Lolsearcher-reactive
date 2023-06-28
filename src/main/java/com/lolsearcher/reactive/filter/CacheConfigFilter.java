package com.lolsearcher.reactive.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Order(-100)
@Component
public class CacheConfigFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        exchange.getResponse().getHeaders().setCacheControl(CacheControl.maxAge(Duration.ofMinutes(1 /* 한 게임당 최소 5분 */)));
        //exchange.getResponse().getHeaders().setCacheControl("public");

        return chain.filter(exchange);
    }
}
