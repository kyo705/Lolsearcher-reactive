package com.lolsearcher.reactive.model.output.match;

import com.lolsearcher.reactive.constant.LolSearcherConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeamDto {

    private short teamId;
    private byte gameResult;
    private final List<Integer> banList = new ArrayList<>(LolSearcherConstants.THE_NUMBER_OF_TEAM_MEMBERS);
    private final List<ParticipantDto> participantDtoList = new ArrayList<>(LolSearcherConstants.THE_NUMBER_OF_TEAM_MEMBERS);
}
