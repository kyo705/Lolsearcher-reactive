package com.lolsearcher.reactive.utils;

import com.lolsearcher.reactive.ingame.dto.InGameBannedChampionDto;
import com.lolsearcher.reactive.ingame.dto.InGameDto;
import com.lolsearcher.reactive.ingame.dto.InGameParticipantDto;
import com.lolsearcher.reactive.ingame.dto.InGamePerksDto;
import com.lolsearcher.reactive.ingame.riotgamesdto.RiotGamesInGameBannedChampionDto;
import com.lolsearcher.reactive.ingame.riotgamesdto.RiotGamesInGameDto;
import com.lolsearcher.reactive.ingame.riotgamesdto.RiotGamesInGameParticipantDto;
import com.lolsearcher.reactive.ingame.riotgamesdto.RiotGamesInGamePerksDto;
import com.lolsearcher.reactive.match.PositionState;
import com.lolsearcher.reactive.match.dto.DetailMemberDto;
import com.lolsearcher.reactive.match.dto.MatchDto;
import com.lolsearcher.reactive.match.dto.PerksDto;
import com.lolsearcher.reactive.match.dto.SummaryMemberDto;
import com.lolsearcher.reactive.match.riotgamesdto.RiotGamesMatchDto;
import com.lolsearcher.reactive.match.riotgamesdto.RiotGamesParticipantDto;
import com.lolsearcher.reactive.match.riotgamesdto.RiotGamesTotalMatchDto;
import com.lolsearcher.reactive.match.riotgamesdto.perk.RiotGamesMatchPerkStatsDto;
import com.lolsearcher.reactive.match.riotgamesdto.perk.RiotGamesMatchPerkStyleDto;
import com.lolsearcher.reactive.rank.*;
import com.lolsearcher.reactive.summoner.RiotGamesSummonerDto;
import com.lolsearcher.reactive.summoner.SummonerDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.TimeZone;

import static com.lolsearcher.reactive.match.MatchResultState.*;
import static com.lolsearcher.reactive.match.PositionState.NONE;
import static com.lolsearcher.reactive.match.TeamState.BLUE;
import static com.lolsearcher.reactive.match.TeamState.RED;
import static com.lolsearcher.reactive.rank.RankConstant.CURRENT_SEASON_ID;

public class ResponseFactory {

    public static SummonerDto getSummonerDto(RiotGamesSummonerDto summoner) {

        return SummonerDto.builder()
                .summonerId(summoner.getId())
                .puuid(summoner.getPuuid())
                .accountId(summoner.getAccountId())
                .summonerName(summoner.getName())
                .summonerLevel(summoner.getSummonerLevel())
                .profileIconId(summoner.getProfileIconId())
                .build();
    }

    public static RankDto getRankDto(RiotGamesRankDto rank) {

        return RankDto.builder()
                .summonerId(rank.getSummonerId())
                .seasonId(CURRENT_SEASON_ID)
                .queueType(RankTypeState.valueOf(rank.getQueueType()))
                .leagueId(rank.getLeagueId())
                .tier(TierState.valueOf(rank.getTier()))
                .rank(RankState.valueOf(rank.getRank()))
                .leaguePoints(rank.getLeaguePoints())
                .wins(rank.getWins())
                .losses(rank.getLosses())
                .build();
    }

    public static MatchDto getMatchDto(RiotGamesTotalMatchDto successMatch) {

        RiotGamesMatchDto riotGamesMatchDto = successMatch.getInfo();

        MatchDto match = new MatchDto();
        match.setMatchId(successMatch.getMetadata().getMatchId());
        match.setGameDuration(LocalTime.ofSecondOfDay(riotGamesMatchDto.getGameDuration()));
        match.setGameEndTimestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(riotGamesMatchDto.getGameEndTimestamp()),  TimeZone.getDefault().toZoneId()));
        match.setQueueId(riotGamesMatchDto.getQueueId());
        match.setSeasonId(CURRENT_SEASON_ID);
        match.setVersion(riotGamesMatchDto.getGameVersion());


        for(RiotGamesParticipantDto participantDto : riotGamesMatchDto.getParticipants()){

            SummaryMemberDto summaryMember = new SummaryMemberDto();
            summaryMember.setTeam(participantDto.getTeamId() == 100 ? RED : BLUE);
            summaryMember.setResult(riotGamesMatchDto.getGameDuration() > 60*10 ? (participantDto.isWin() ? WIN : LOSS) : DRAW);
            summaryMember.setSummonerId(participantDto.getSummonerId());
            summaryMember.setPickChampionId(participantDto.getChampionId());
            summaryMember.setPosition(participantDto.getTeamPosition().isEmpty() ? NONE : PositionState.valueOf(participantDto.getTeamPosition()));
            summaryMember.setChampionLevel(participantDto.getChampLevel());
            summaryMember.setMinionKills((short) (participantDto.getNeutralMinionsKilled() + participantDto.getTotalMinionsKilled()));
            summaryMember.setKills(participantDto.getKills());
            summaryMember.setDeaths(participantDto.getDeaths());
            summaryMember.setAssists(participantDto.getAssists());
            summaryMember.setItem0(participantDto.getItem0());
            summaryMember.setItem1(participantDto.getItem1());
            summaryMember.setItem2(participantDto.getItem2());
            summaryMember.setItem3(participantDto.getItem3());
            summaryMember.setItem4(participantDto.getItem4());
            summaryMember.setItem5(participantDto.getItem5());
            summaryMember.setItem6(participantDto.getItem6());

            match.getSummaryMember().add(summaryMember);

            List<RiotGamesMatchPerkStyleDto> perkStyleDtos = participantDto.getPerks().getStyles();
            PerksDto perks = new PerksDto();
            perks.setMainPerkStyle(perkStyleDtos.get(0).getStyle());
            perks.setSubPerkStyle(perkStyleDtos.get(1).getStyle());
            perks.setMainPerk1(perkStyleDtos.get(0).getSelections().get(0).getPerk());
            perks.setMainPerk2(perkStyleDtos.get(0).getSelections().get(1).getVar1());
            perks.setMainPerk3(perkStyleDtos.get(0).getSelections().get(2).getVar1());
            perks.setMainPerk4(perkStyleDtos.get(0).getSelections().get(3).getVar1());
            perks.setSubPerk1(perkStyleDtos.get(1).getSelections().get(0).getVar1());
            perks.setSubPerk2(perkStyleDtos.get(1).getSelections().get(1).getVar1());

            RiotGamesMatchPerkStatsDto perkStatsDto = participantDto.getPerks().getStatPerks();
            perks.setDefense(perkStatsDto.getDefense());
            perks.setOffense(perkStatsDto.getOffense());
            perks.setFlex(perkStatsDto.getFlex());

            summaryMember.setPerksDto(perks);

            DetailMemberDto detailMember = new DetailMemberDto();
            detailMember.setGoldEarned(participantDto.getGoldEarned());
            detailMember.setGoldSpent(participantDto.getGoldSpent());
            detailMember.setTotalDamageDealt(participantDto.getTotalDamageDealt());
            detailMember.setTotalDamageDealtToChampions(participantDto.getTotalDamageDealtToChampions());
            detailMember.setTotalDamageShieldedOnTeammates(participantDto.getTotalDamageShieldedOnTeammates());
            detailMember.setTotalDamageTaken(participantDto.getTotalDamageTaken());
            detailMember.setTimeCCingOthers(participantDto.getTimeCCingOthers());
            detailMember.setTotalHeal(participantDto.getTotalHeal());
            detailMember.setTotalHealsOnTeammates(participantDto.getTotalHealsOnTeammates());
            detailMember.setDetectorWardPurchased(participantDto.getVisionWardsBoughtInGame());
            detailMember.setDetectorWardsPlaced(participantDto.getWardsPlaced());
            detailMember.setWardKills(participantDto.getWardsKilled());
            detailMember.setWardsPlaced(participantDto.getWardsPlaced());

            summaryMember.setDetailMemberDto(detailMember);
        }

        riotGamesMatchDto.getTeams().forEach(team -> team.getBans().forEach(
                ban -> match.getSummaryMember().get(ban.getPickTurn()-1).setBanChampionId(ban.getChampionId())));

        return match;
    }

    public static InGameDto getInGameDto(RiotGamesInGameDto riotGamesInGameDto){

        if(riotGamesInGameDto == null){
            return null;
        }
        InGameDto inGameDto = new InGameDto();
        inGameDto.setGameId(riotGamesInGameDto.getGameId());
        inGameDto.setGameType(riotGamesInGameDto.getGameType());
        inGameDto.setGameStartTime(riotGamesInGameDto.getGameStartTime());
        inGameDto.setMapId(riotGamesInGameDto.getMapId());
        inGameDto.setGameLength(riotGamesInGameDto.getGameLength());
        inGameDto.setPlatformId(riotGamesInGameDto.getPlatformId());
        inGameDto.setGameMode(riotGamesInGameDto.getGameMode());
        inGameDto.setGameQueueConfigId(riotGamesInGameDto.getGameQueueConfigId());

        if(riotGamesInGameDto.getBannedChampions() != null){
            for(RiotGamesInGameBannedChampionDto bannedChampionInfo : riotGamesInGameDto.getBannedChampions()){

                InGameBannedChampionDto inGameBannedChampionDto = InGameBannedChampionDto.builder()
                        .championId(bannedChampionInfo.getChampionId())
                        .pickTurn(bannedChampionInfo.getPickTurn())
                        .teamId(bannedChampionInfo.getTeamId())
                        .build();

                inGameDto.getBannedChampions().add(inGameBannedChampionDto);
            }
        }

        if(riotGamesInGameDto.getParticipants() != null){
            for(RiotGamesInGameParticipantDto riotGamesInGameParticipantDto : riotGamesInGameDto.getParticipants()){

                InGameParticipantDto inGameParticipantDto = InGameParticipantDto.builder()
                        .championId(riotGamesInGameParticipantDto.getChampionId())
                        .profileIconId(riotGamesInGameParticipantDto.getProfileIconId())
                        .bot(riotGamesInGameParticipantDto.isBot())
                        .teamId(riotGamesInGameParticipantDto.getTeamId())
                        .summonerName(riotGamesInGameParticipantDto.getSummonerName())
                        .summonerId(riotGamesInGameParticipantDto.getSummonerId())
                        .spell1Id(riotGamesInGameParticipantDto.getSpell1Id())
                        .spell2Id(riotGamesInGameParticipantDto.getSpell2Id())
                        .build();

                inGameDto.getParticipants().add(inGameParticipantDto);

                RiotGamesInGamePerksDto riotGamesInGamePerksDto = riotGamesInGameParticipantDto.getPerks();
                if(riotGamesInGamePerksDto != null){
                    InGamePerksDto inGamePerksDto = InGamePerksDto.builder()
                            .perkStyle(riotGamesInGamePerksDto.getPerkStyle())
                            .perkSubStyle(riotGamesInGamePerksDto.getPerkSubStyle())
                            .mainPerk1(riotGamesInGamePerksDto.getPerkIds().get(0))
                            .mainPerk2(riotGamesInGamePerksDto.getPerkIds().get(1))
                            .mainPerk3(riotGamesInGamePerksDto.getPerkIds().get(2))
                            .mainPerk4(riotGamesInGamePerksDto.getPerkIds().get(3))
                            .subPerk1(riotGamesInGamePerksDto.getPerkIds().get(4))
                            .subPerk2(riotGamesInGamePerksDto.getPerkIds().get(5))
                            .statPerk1(riotGamesInGamePerksDto.getPerkIds().get(6))
                            .statPerk2(riotGamesInGamePerksDto.getPerkIds().get(7))
                            .statPerk3(riotGamesInGamePerksDto.getPerkIds().get(8))
                            .build();

                    inGameParticipantDto.setPerks(inGamePerksDto);
                }
            }
        }
        return inGameDto;
    }
}
