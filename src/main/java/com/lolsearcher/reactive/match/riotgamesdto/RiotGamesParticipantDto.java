package com.lolsearcher.reactive.match.riotgamesdto;

import com.lolsearcher.reactive.match.riotgamesdto.perk.RiotGamesMatchPerksDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import static com.google.common.base.Preconditions.checkArgument;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_ID_MAX_LENGTH;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_ID_MIN_LENGTH;
import static com.lolsearcher.reactive.utils.RiotGamesDataCacheKeyUtils.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Setter
public class RiotGamesParticipantDto {
    private Short assists;
    private Byte baronKills;
    private Byte bountyLevel;
    private int champExperience;
    private Byte champLevel;
    private Short championId;
    private String championName;
    private int championTransform;
    private int consumablesPurchased;
    private int damageDealtToBuildings;
    private int damageDealtToObjectives;
    private int damageDealtToTurrets;
    private int damageSelfMitigated;
    private Short deaths;
    private Short detectorWardsPlaced;
    private Short doubleKills;
    private Byte dragonKills;
    private boolean firstBloodAssist;
    private boolean firstBloodKill;
    private boolean firstTowerAssist;
    private boolean firstTowerKill;
    private boolean gameEndedInEarlySurrender;
    private boolean gameEndedInSurrender;
    private int goldEarned;
    private int goldSpent;
    private String individualPosition;
    private Byte inhibitorTakedowns;
    private Byte inhibitorsLost;
    private Short item0;
    private Short item1;
    private Short item2;
    private Short item3;
    private Short item4;
    private Short item5;
    private Short item6;
    private Short itemsPurchased;
    private int killingSprees;
    private Short kills;
    private String lane;
    private int largestCriticalStrike;
    private int largestKillingSpree;
    private int largestMultiKill;
    private int longestTimeSpentLiving;
    private int magicDamageDealt;
    private int magicDamageDealtToChampions;
    private int magicDamageTaken;
    private short neutralMinionsKilled;
    private Byte nexusKills;
    private Byte nexusTakedowns;
    private Byte nexusLost;
    private int objectivesStolen;
    private int objectivesStolenAssists;
    private int participantId;
    private Short pentaKills;
    private RiotGamesMatchPerksDto perks;
    private int physicalDamageDealt;
    private int physicalDamageDealtToChampions;
    private int physicalDamageTaken;
    private int profileIcon;
    private String puuid;
    private Short quadraKills;
    private String riotIdName;
    private String riotIdTagline;
    private String role;
    private Short sightWardsBoughtInGame;
    private Short spell1Casts;
    private Short spell2Casts;
    private Short spell3Casts;
    private Short spell4Casts;
    private Short summoner1Casts;
    private Short summoner1Id;
    private Short summoner2Casts;
    private Short summoner2Id;
    private String summonerId;
    private int summonerLevel;
    private String summonerName;
    private boolean teamEarlySurrendered;
    private Short teamId;
    private String teamPosition;
    private int timeCCingOthers;
    private int timePlayed;
    private int totalDamageDealt;
    private int totalDamageDealtToChampions;
    private int totalDamageShieldedOnTeammates;
    private int totalDamageTaken;
    private int totalHeal;
    private int totalHealsOnTeammates;
    private short totalMinionsKilled;
    private int totalTimeCCDealt;
    private int totalTimeSpentDead;
    private int totalUnitsHealed;
    private Short tripleKills;
    private int trueDamageDealt;
    private int trueDamageDealtToChampions;
    private int trueDamageTaken;
    private Byte turretKills;
    private Byte turretTakedowns;
    private Byte turretsLost;
    private Short unrealKills;
    private Short visionScore;
    private Short visionWardsBoughtInGame;
    private Short wardsKilled;
    private Short wardsPlaced;
    private boolean win;

    public Mono<RiotGamesParticipantDto> validate(ReactiveStringRedisTemplate template) {

        return Mono.just("DUMMY")
                .doOnNext(obj -> checkArgument(isNotEmpty(summonerId) && summonerId.length() >= SUMMONER_ID_MIN_LENGTH && summonerId.length() <= SUMMONER_ID_MAX_LENGTH,
                        String.format("summonerId must be provided and its length must be between %s and %s", SUMMONER_ID_MIN_LENGTH, SUMMONER_ID_MAX_LENGTH)
                ))
                .doOnNext(obj -> checkArgument(champLevel >=1 && champLevel <= 18, "championLevel must be between 1 and 18"))
                .doOnNext(obj -> checkArgument(neutralMinionsKilled >= 0, "minionKills must be positive"))
                .doOnNext(obj -> checkArgument(totalMinionsKilled >= 0, "minionKills must be positive"))
                .doOnNext(obj -> checkArgument(kills >= 0, "kills must be positive"))
                .doOnNext(obj -> checkArgument(deaths >= 0, "deaths must be positive"))
                .doOnNext(obj -> checkArgument(assists >= 0, "assists must be positive"))
                .doOnNext(obj -> checkArgument(goldEarned >= 0, "goldEarned must be positive"))
                .doOnNext(obj -> checkArgument(goldSpent >= 0, "goldSpent must be positive"))
                .doOnNext(obj -> checkArgument(totalDamageDealt >= 0, "totalDamageDealt must be positive"))
                .doOnNext(obj ->  checkArgument(totalDamageDealtToChampions >= 0, "totalDamageDealtToChampions must be positive"))
                .doOnNext(obj -> checkArgument(totalDamageShieldedOnTeammates >= 0, "totalDamageShieldedOnTeammates must be positive"))
                .doOnNext(obj -> checkArgument(totalDamageTaken >= 0, "totalDamageTaken must be positive"))
                .doOnNext(obj -> checkArgument(timeCCingOthers >= 0, "timeCCingOthers must be positive"))
                .doOnNext(obj -> checkArgument(totalHeal >= 0, "totalHeal must be positive"))
                .doOnNext(obj -> checkArgument(totalHealsOnTeammates >= 0, "totalHealsOnTeammates must be positive"))
                .doOnNext(obj -> checkArgument(visionWardsBoughtInGame >= 0, "detectorWardPurchased must be positive"))
                .doOnNext(obj -> checkArgument(detectorWardsPlaced >= 0, "detectorWardsPlaced must be positive"))
                .doOnNext(obj -> checkArgument(wardsKilled >= 0, "wardKills must be positive"))
                .doOnNext(obj -> checkArgument(wardsPlaced >= 0, "wardsPlaced must be positive"))
                .flatMap(obj -> template.opsForValue().get(getItemKey(item0)).switchIfEmpty(Mono.error(new IllegalArgumentException("itemId must be in permitted boundary" + getItemKey(item0)))))
                .flatMap(obj -> template.opsForValue().get(getItemKey(item1)).switchIfEmpty(Mono.error(new IllegalArgumentException("itemId must be in permitted boundary"+ getItemKey(item1)))))
                .flatMap(obj -> template.opsForValue().get(getItemKey(item2)).switchIfEmpty(Mono.error(new IllegalArgumentException("itemId must be in permitted boundary"+ getItemKey(item2)))))
                .flatMap(obj -> template.opsForValue().get(getItemKey(item3)).switchIfEmpty(Mono.error(new IllegalArgumentException("itemId must be in permitted boundary"+ getItemKey(item3)))))
                .flatMap(obj -> template.opsForValue().get(getItemKey(item4)).switchIfEmpty(Mono.error(new IllegalArgumentException("itemId must be in permitted boundary"+ getItemKey(item4)))))
                .flatMap(obj -> template.opsForValue().get(getItemKey(item5)).switchIfEmpty(Mono.error(new IllegalArgumentException("itemId must be in permitted boundary"+ getItemKey(item5)))))
                .flatMap(obj -> template.opsForValue().get(getItemKey(item6)).switchIfEmpty(Mono.error(new IllegalArgumentException("itemId must be in permitted boundary"+ getItemKey(item6)))))
                .flatMap(obj -> template.opsForValue().get(getChampionKey(championId)).switchIfEmpty(Mono.error(new IllegalArgumentException("championId must be in permitted boundary"))))
                .flatMap(obj -> template.opsForValue().get(getSpellKey(summoner1Id)).switchIfEmpty(Mono.error(new IllegalArgumentException("spellId must be in permitted boundary"))))
                .flatMap(obj -> template.opsForValue().get(getSpellKey(summoner2Id)).switchIfEmpty(Mono.error(new IllegalArgumentException("spellId must be in permitted boundary"))))
                .flatMap(obj -> perks.validate(template))
                .map(obj -> this);
    }
}
