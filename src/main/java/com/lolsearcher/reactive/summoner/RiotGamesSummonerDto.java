package com.lolsearcher.reactive.summoner;

import lombok.Getter;
import lombok.Setter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.lolsearcher.reactive.summoner.SummonerConstant.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@Setter
public class RiotGamesSummonerDto {

    private String accountId;
    private int profileIconId;
    private long revisionDate;
    private String name;
    private String id;
    private String puuid;
    private long summonerLevel;

    public void validate() {

        checkArgument(
                isNotBlank(id) && id.length() >= SUMMONER_ID_MIN_LENGTH && id.length() <= SUMMONER_ID_MAX_LENGTH,
                String.format("summonerId must be provided and its length must be between %s and %s", SUMMONER_ID_MIN_LENGTH, SUMMONER_ID_MAX_LENGTH)
        );
        checkArgument(
                isNotBlank(accountId) && accountId.length() >= ACCOUNT_ID_MIN_LENGTH && accountId.length() <= ACCOUNT_ID_MAX_LENGTH,
                String.format("summonerId must be provided and its length must be between %s and %s", ACCOUNT_ID_MIN_LENGTH, ACCOUNT_ID_MAX_LENGTH)
        );
        checkArgument(
                isNotBlank(puuid) && puuid.length() >= PUUID_MIN_LENGTH && puuid.length() <= PUUID_MAX_LENGTH,
                String.format("puuid must be provided and its length must be between %s and %s", PUUID_MIN_LENGTH, PUUID_MAX_LENGTH)
        );
        checkArgument(
                isNotBlank(name) && name.length() >= SUMMONER_NAME_MIN_LENGTH && name.length() <= SUMMONER_NAME_MAX_LENGTH,
                String.format("summonerName must be provided and its length must be between %s and %s", SUMMONER_NAME_MIN_LENGTH, SUMMONER_NAME_MAX_LENGTH)
        );
        checkArgument( profileIconId >= 0, "profileIconId must be positive");
        checkArgument(summonerLevel >= 0, "summoner level must be positive");
    }
}
