package com.lolsearcher.reactive.filter;

import com.lolsearcher.reactive.service.ban.BanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class SearchBanFilter implements WebFilter {

    private final BanService banService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return Mono.just(exchange)
                .flatMap(banService::inspect)
                .flatMap(str->chain.filter(exchange));
    }
}
