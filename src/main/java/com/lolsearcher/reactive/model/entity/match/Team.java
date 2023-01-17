package com.lolsearcher.reactive.model.entity.match;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.lolsearcher.reactive.constant.LolSearcherConstants.THE_NUMBER_OF_TEAM_MEMBERS;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "match")
public class Team implements Serializable {

    private Long id;
    private byte gameResult;    /*  0 : win,  1 : loss,  2 : draw  */
    private short teamPositionId; /* 100 : red, 200 : blue */

    private Match match;

    private List<SummaryMember> members = new ArrayList<>(THE_NUMBER_OF_TEAM_MEMBERS);

    public void setMatch(Match match) throws IllegalAccessException {

        if(match.getTeams().size() >= 2){
            throw new IllegalAccessException("이미 연관관계 설정이 된 Match 객체입니다.");
        }
        this.match = match;
        match.getTeams().add(this);
    }
}
