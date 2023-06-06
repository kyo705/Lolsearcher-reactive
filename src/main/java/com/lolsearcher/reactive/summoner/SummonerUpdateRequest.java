package com.lolsearcher.reactive.summoner;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_NAME_MAX_LENGTH;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_NAME_MIN_LENGTH;

@Data
public class SummonerUpdateRequest {

    @NotBlank @Size(max = SUMMONER_NAME_MAX_LENGTH, min = SUMMONER_NAME_MIN_LENGTH) String realSummonerName;

    List<String> summonerIds;
}
