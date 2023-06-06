package com.lolsearcher.reactive.rank;

import lombok.Getter;
import lombok.Setter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.lolsearcher.reactive.rank.RankConstant.*;
import static com.lolsearcher.reactive.rank.RankState.NONE;
import static com.lolsearcher.reactive.rank.TierState.*;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_ID_MAX_LENGTH;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_ID_MIN_LENGTH;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Setter
public class RiotGamesRankDto {

    private String leagueId;
    private String summonerId;
    private String summonerName;
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private boolean hotStreak;
    private boolean veteran;
    private boolean freshBlood;
    private boolean inactive;

    public void validate() {

        checkArgument(
                isNotEmpty(summonerId) && summonerId.length() >= SUMMONER_ID_MIN_LENGTH && summonerId.length() <= SUMMONER_ID_MAX_LENGTH,
                String.format("summonerId must be provided and its length must be between %s and %s", SUMMONER_ID_MIN_LENGTH, SUMMONER_ID_MAX_LENGTH)
        );

        checkArgument(queueType != null, "queueType must be provided");

        checkArgument(
                (leagueId.length() >= LEAGUE_ID_MIN_LENGTH && leagueId.length() <= LEAGUE_ID_MAX_LENGTH) || isEmpty(leagueId),
                String.format("leagueId must be NULL or its length must be between %s and %s", LEAGUE_ID_MIN_LENGTH, LEAGUE_ID_MAX_LENGTH)
        );

        checkArgument(
                leaguePoints >= 0 && (leaguePoints <= 100 || TierState.valueOf(tier) == CHALLENGER || TierState.valueOf(tier) == GRANDMASTER || TierState.valueOf(tier) == MASTER),
                "leaguePoints must be in boundary point"
        );

        checkArgument(
                ((TierState.valueOf(tier) == CHALLENGER || TierState.valueOf(tier) == GRANDMASTER || TierState.valueOf(tier) == MASTER || TierState.valueOf(tier) == UNRANK) && RankState.valueOf(rank) == NONE) ||
                        (TierState.valueOf(tier) != CHALLENGER && TierState.valueOf(tier) != GRANDMASTER && TierState.valueOf(tier) != MASTER && TierState.valueOf(tier) != UNRANK && RankState.valueOf(rank) != NONE),
                "tier rank must be collaborate properly"
        );

        checkArgument(wins >= 0, "wins must be positive");

        checkArgument(losses >= 0, "losses must be positive");
    }
}
