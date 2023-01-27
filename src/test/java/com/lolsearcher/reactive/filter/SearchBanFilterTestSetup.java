package com.lolsearcher.reactive.filter;

import com.lolsearcher.reactive.model.output.ingame.InGameDto;

public class SearchBanFilterTestSetup {
    protected static InGameDto getInGameDto() {

        InGameDto inGameDto = new InGameDto();
        inGameDto.setGameId(1000);

        return inGameDto;
    }
}
