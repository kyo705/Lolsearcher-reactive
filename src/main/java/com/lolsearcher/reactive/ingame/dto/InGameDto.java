package com.lolsearcher.reactive.ingame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class InGameDto implements Serializable {

	private long gameId;
	private String gameType;
	private long gameStartTime;
	private long mapId;
	private long gameLength;
	private String platformId;
	private String gameMode;
	private List<InGameBannedChampionDto> bannedChampions = new ArrayList<>();
	private long gameQueueConfigId;
	private List<InGameParticipantDto> participants = new ArrayList<>();
}

