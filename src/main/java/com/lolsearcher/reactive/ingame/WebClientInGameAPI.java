package com.lolsearcher.reactive.ingame;

import com.lolsearcher.reactive.cache.ReactiveRedisCacheable;
import com.lolsearcher.reactive.errors.exception.IllegalRiotGamesResponseDataException;
import com.lolsearcher.reactive.ingame.riotgamesdto.RiotGamesInGameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.codec.DecodingException;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.ingame.InGameConstant.IN_GAME_CACHE_KEY;
import static com.lolsearcher.reactive.ingame.InGameConstant.RIOTGAMES_INGAME_WITH_ID_URI;

@RequiredArgsConstructor
@Component
public class WebClientInGameAPI implements InGameAPI {

    private final ReactiveStringRedisTemplate template;
    private final WebClient koreaWebClient;
    @Value("${lolsearcher.riot_api_key}") private String key;

    @ReactiveRedisCacheable(name = IN_GAME_CACHE_KEY, key = "#summonerId", ttl = "${lolsearcher.cache.ingame.ttl}")
    @Override
    public Mono<RiotGamesInGameDto> findById(String summonerId) {

        return koreaWebClient
                .get()
                .uri(RIOTGAMES_INGAME_WITH_ID_URI, summonerId, key)
                .retrieve()
                .bodyToMono(RiotGamesInGameDto.class)
                .flatMap(dto -> dto.validate(template))
                .onErrorResume(throwable -> {
                    if(throwable instanceof  IllegalArgumentException || throwable instanceof DecodingException){
                        return Mono.error(new IllegalRiotGamesResponseDataException(throwable.getMessage()));
                    }
                    else if(throwable instanceof WebClientResponseException) {
                        WebClientResponseException webClientResponseException = (WebClientResponseException) throwable;
                        if(webClientResponseException.getStatusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.empty();
                        }
                    }
                    return Mono.error(throwable);
                });
    }
}
