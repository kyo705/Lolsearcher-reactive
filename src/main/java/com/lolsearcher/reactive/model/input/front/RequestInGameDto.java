package com.lolsearcher.reactive.model.input.front;

import lombok.*;

import javax.validation.constraints.NotEmpty;


@AllArgsConstructor
@Data
public class RequestInGameDto {

    @NotEmpty
    private final String summonerId;

    public RequestInGameDto(){
        summonerId = "";
    }
}
