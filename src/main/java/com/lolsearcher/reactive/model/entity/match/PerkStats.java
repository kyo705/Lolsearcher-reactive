package com.lolsearcher.reactive.model.entity.match;

import lombok.Data;

import java.io.Serializable;

@Data
public class PerkStats implements Serializable {

    private Integer id;

    private Short defense;
    private Short flex;
    private Short offense;

}
