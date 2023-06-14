package com.lolsearcher.reactive.match.riotgamesdto;

import com.lolsearcher.reactive.match.riotgamesdto.team.RiotGamesTeamDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getQueueKey;

@Getter
@Setter
public class RiotGamesMatchDto implements Serializable {
    private long gameCreation;
    private long gameDuration;
    private long gameEndTimestamp;
    private long gameId;
    private String gameMode;
    private String gameName;
    private long gameStartTimestamp;
    private String gameType;
    private String gameVersion;
    private int mapId;
    private List<RiotGamesParticipantDto>  participants;
    private String platformId;
    private int queueId;
    private List<RiotGamesTeamDto> teams;
    private String tournamentCode;

    public Mono<RiotGamesMatchDto> validate(ReactiveStringRedisTemplate template) {

        return Mono.just("DUMMY")
                .flatMap(obj -> template.opsForValue()
                        .get(getQueueKey(queueId))
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("queueId must be in permitted boundary")))
                )
                .doOnNext(obj -> checkArgument(gameId > 0, "gameId must be positive"))
                .doOnNext(obj -> checkArgument(gameCreation > 0, "gameCreation must be positive"))
                .doOnNext(obj -> checkArgument(gameDuration > 0, "gameDuration must be positive"))
                .doOnNext(obj -> checkArgument(gameEndTimestamp > 0 && gameEndTimestamp < System.currentTimeMillis(),
                        "gameEndTimestamp must be less than current timestamp"))
                .doOnNext(obj -> checkArgument(gameId > 0, "gameId must be positive"))
                .flatMapIterable(obj -> participants).flatMap(participant -> participant.validate(template)).collectList()
                .flatMapIterable(obj -> teams).flatMap(team -> team.validate(template)).collectList()
                .map(obj -> this);
    }
}
