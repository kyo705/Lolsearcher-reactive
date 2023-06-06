package com.lolsearcher.reactive.rank;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum RankState {

    I(1),
    II(2),
    III(3),
    IV(4),
    V(5),
    NONE(6);

    private final int id;

    RankState(int id) {
        this.id = id;
    }

    private static final Map<Integer, RankState> BY_NUMBER =
            Stream.of(values()).collect(Collectors.toMap(RankState::getId, e -> e));

    public static RankState valueOfId(int code){
        return BY_NUMBER.get(code);
    }
}

