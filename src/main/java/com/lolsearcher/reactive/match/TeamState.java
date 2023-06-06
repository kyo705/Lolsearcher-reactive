package com.lolsearcher.reactive.match;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum TeamState {

    RED(0),
    BLUE(1);

    private final int code;

    TeamState(int code) {
        this.code = code;
    }

    private static final Map<Integer, TeamState> BY_NUMBER =
            Stream.of(values()).collect(Collectors.toMap(TeamState::getCode, e -> e));

    public static TeamState valueOfCode(int code){
        return BY_NUMBER.get(code);
    }
}
