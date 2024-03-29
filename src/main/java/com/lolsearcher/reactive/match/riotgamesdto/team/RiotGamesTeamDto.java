package com.lolsearcher.reactive.match.riotgamesdto.team;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
@Setter
public class RiotGamesTeamDto {
    private List<RiotGamesTeamBanDto> bans;
    private RiotGamesTeamObjectivesDto objectives;
    private short teamId;
    private boolean win;

    public Mono<RiotGamesTeamDto> validate(ReactiveStringRedisTemplate template) {

        return Mono.just("DUMMY")
                .doOnNext(obj -> checkArgument(teamId == 100 || teamId == 200, "teamId must be in permitted boundary"))
                .map(obj -> this);
    }
}
