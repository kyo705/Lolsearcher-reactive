package com.lolsearcher.reactive.model.entity.match;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.lolsearcher.reactive.constant.LolSearcherConstants.THE_NUMBER_OF_TEAMS;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Match implements Serializable {

	private Long id;
	private String matchId; /* REST API로 받아올 때 필요한 고유한 match id */
	private long gameDuration;
	private long gameEndTimestamp;
	private int queueId;
	private int seasonId;
	private String version;

	@JsonManagedReference
	private List<Team> teams = new ArrayList<>(THE_NUMBER_OF_TEAMS);

}
