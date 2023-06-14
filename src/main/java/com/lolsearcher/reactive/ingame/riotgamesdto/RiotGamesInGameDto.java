package com.lolsearcher.reactive.ingame.riotgamesdto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getQueueKey;

@Getter
@Setter
public class RiotGamesInGameDto {

    long gameId;
    String gameType;
    long gameQueueConfigId;
    long gameStartTime;
    long mapId;
    long gameLength;
    String platformId;
    String gameMode;
    List<RiotGamesInGameBannedChampionDto> bannedChampions;
    List<RiotGamesInGameParticipantDto> participants;

    public Mono<RiotGamesInGameDto> validate(ReactiveStringRedisTemplate template) {

        return Mono.just(" ")
                .flatMap(obj -> template.opsForValue().get(getQueueKey(gameQueueConfigId)).switchIfEmpty(Mono.error(new IllegalArgumentException("gameQueueConfigId must be in permitted boundary"))))
                .doOnNext(obj -> checkArgument(gameId > 0, "gameId must be positive"))
                .doOnNext(obj -> checkArgument(gameLength > 0, "gameLength must be positive"))
                .doOnNext(obj -> checkArgument(gameStartTime > 0 && gameStartTime < System.currentTimeMillis(), "gameStartTime must be positive and less than current timestamp"))
                .flatMapIterable(obj -> bannedChampions).flatMap(bannedChampion -> bannedChampion.validate(template)).collectList()
                .flatMapIterable(obj -> participants).flatMap(participant -> participant.validate(template)).collectList()
                .map(obj -> this);
    }
}
