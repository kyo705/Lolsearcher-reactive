package com.lolsearcher.reactive.summoner;

import lombok.Data;

import java.util.List;

@Data
public class SummonerUpdateRequest {

    List<String> summonerIds;
}
