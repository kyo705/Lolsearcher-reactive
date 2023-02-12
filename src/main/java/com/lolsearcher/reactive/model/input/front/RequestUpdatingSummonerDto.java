package com.lolsearcher.reactive.model.input.front;

import lombok.Data;

import java.util.List;

@Data
public class RequestUpdatingSummonerDto {

    String realSummonerName;

    List<String> summonerIds;
}
