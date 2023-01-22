package com.lolsearcher.reactive.model.input.riotgames.ingame;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RiotGamesInGamePerksDto {

    List<Short> perkIds; /* 1~4: mainPerkIds 5~6: subPerkIds 7~9: statIds  */
    Short perkStyle;
    Short perkSubStyle;
}
