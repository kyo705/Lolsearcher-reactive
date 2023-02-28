package com.lolsearcher.reactive.integration.controller;

import com.lolsearcher.reactive.model.input.front.RequestMatchDto;
import com.lolsearcher.reactive.model.output.match.MatchDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class MatchControllerTestSetup {

    protected static RequestMatchDto getRequestDto(){

        return RequestMatchDto.builder()
                .summonerId("summonerId")
                .championId(-1)
                .queueId(-1)
                .lastMatchId("")
                .matchCount(20)
                .build();
    }

    protected static Flux<MatchDto> getMatchDtoFlux() {

        List<MatchDto> matchDtoList = new ArrayList<>();

        for(int i=0;i<3;i++){
            MatchDto matchDto = new MatchDto();
            matchDto.setVersion("2");
            matchDto.setQueueId(420);
            matchDto.setGameDuration(15000000L);
            matchDtoList.add(matchDto);
        }

        return Flux.fromIterable(matchDtoList);
    }

    public static WebClientResponseException get404Error() {

        return new WebClientResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        null,null,null);
    }
}
