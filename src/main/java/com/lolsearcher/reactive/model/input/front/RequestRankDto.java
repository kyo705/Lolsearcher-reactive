package com.lolsearcher.reactive.model.input.front;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RequestRankDto {

    @NotEmpty
    private final String summonerId;

    public RequestRankDto() {
        this.summonerId = "";
    }
}
