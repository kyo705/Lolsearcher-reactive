package com.lolsearcher.reactive.match.riotgamesdto;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.lolsearcher.reactive.summoner.SummonerConstant.PUUID_MAX_LENGTH;
import static com.lolsearcher.reactive.summoner.SummonerConstant.PUUID_MIN_LENGTH;

@Getter
@Setter
public class RiotGamesMatchMetadataDto {
    private String dataVersion;
    private String matchId;
    private List<String> participants;   // puuid list

    public Mono<RiotGamesMatchMetadataDto> validate() {

        return Mono.just("DUMMY")
                .doOnNext(obj -> participants.forEach(participant -> checkArgument(
                        participant.length() >= PUUID_MIN_LENGTH && participant.length() <= PUUID_MAX_LENGTH,
                                "gameId must be positive")))
                .map(obj -> this);
    }
}
