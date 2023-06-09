package com.lolsearcher.reactive.ingame.riotgamesdto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.lolsearcher.reactive.summoner.SummonerConstant.*;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getChampionKey;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.getSpellKey;

@Getter
@Setter
public class RiotGamesInGameParticipantDto {

    long championId;
    long profileIconId;
    boolean bot;
    long teamId;
    String summonerName;
    String summonerId;
    long spell1Id;
    long spell2Id;
    RiotGamesInGamePerksDto perks;
    List<RiotGamesInGameCustomizationObjectDto> gameCustomizationObjects;

    public Mono<RiotGamesInGameParticipantDto> validate(ReactiveRedisTemplate<String, Object> template) {

        return Mono.just(" ")
                .doOnNext(obj -> checkArgument(summonerName.length() >= SUMMONER_NAME_MIN_LENGTH && summonerName.length() <= SUMMONER_NAME_MAX_LENGTH,
                        String.format("summonerName length must be between %s and %s", SUMMONER_NAME_MIN_LENGTH, SUMMONER_NAME_MAX_LENGTH)))
                .doOnNext(obj -> checkArgument(summonerId.length() >= SUMMONER_ID_MIN_LENGTH && summonerId.length() <= SUMMONER_ID_MAX_LENGTH,
                        String.format("summonerId length must be between %s and %s", SUMMONER_ID_MIN_LENGTH, SUMMONER_ID_MAX_LENGTH)))
                .doOnNext(obj -> checkArgument(teamId == 100 /* red */ || teamId == 200 /* blue */, "teamId must be 100 or 200"))
                .flatMap(obj -> template.opsForValue().get(getChampionKey(championId))
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("championId must be in permitted boundary"))))
                .flatMap(obj -> template.opsForValue().get(getSpellKey(spell1Id))
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("spellId must be in permitted boundary"))))
                .flatMap(obj -> template.opsForValue().get(getSpellKey(spell2Id))
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("spellId must be in permitted boundary"))))
                .flatMap(obj -> perks.validate(template))
                .map(obj -> this);
    }
}
