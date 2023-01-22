package com.lolsearcher.reactive.model.input.front;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
public class RequestInGameDto {

    @NotEmpty
    private final String summonerId;

    public RequestInGameDto(){
        summonerId = "";
    }
}
