package com.lolsearcher.reactive.model.entity.match;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.lolsearcher.reactive.constant.constant.LolSearcherConstants.THE_NUMBER_OF_TEAM_MEMBERS;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "match")
@EqualsAndHashCode
public class Team implements Serializable {

    private Long id;
    private byte gameResult;    /*  0 : win,  1 : loss,  2 : draw  */
    private short teamPositionId; /* 100 : red, 200 : blue */

    @JsonBackReference
    private Match match;

    @JsonManagedReference
    private List<SummaryMember> members = new ArrayList<>(THE_NUMBER_OF_TEAM_MEMBERS);

    public void setMatch(Match match) throws IllegalAccessException {

        if(match.getTeams().size() >= 2){
            throw new IllegalAccessException("이미 연관관계 설정이 된 Match 객체입니다.");
        }
        this.match = match;
        match.getTeams().add(this);
    }
}
