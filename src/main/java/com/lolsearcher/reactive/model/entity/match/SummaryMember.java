package com.lolsearcher.reactive.model.entity.match;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = "team")
@EqualsAndHashCode
public class SummaryMember implements Serializable {

    private Long id;
    private String summonerId;
    private int banChampionId;
    private int pickChampionId;
    private short positionId;
    private short championLevel; /* level : 1 ~ 18 */
    private short minionKills; /* lineMinionKills + NeutralMinionKills */
    private short kills;
    private short deaths;
    private short assists;
    private short item0;  /* item 리스트(item0 ~ item6)를 반정규화한 이유 : 아이템의 순서가 중요 */
    private short item1;
    private short item2;
    private short item3;
    private short item4;
    private short item5;
    private short item6;

    @JsonManagedReference
    private Perks perks;  /* 해당 게임의 특정 유저가 선택한 스펠, 룬 특성 */

    @JsonManagedReference
    private DetailMember detailMember;

    @JsonBackReference
    private Team team;

    public void setTeam(Team team) throws IllegalAccessException {

        if(team.getMembers().size() >= 5){
            throw new IllegalAccessException("이미 연관관계 설정이 된 Team 객체입니다.");
        }
        this.team = team;
        team.getMembers().add(this);
    }
}
