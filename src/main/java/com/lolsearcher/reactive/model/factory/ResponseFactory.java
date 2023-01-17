package com.lolsearcher.reactive.model.factory;

import com.lolsearcher.reactive.model.entity.match.*;
import com.lolsearcher.reactive.model.output.match.*;

public class ResponseFactory {

    public static MatchDto getResponseMatchDtoFromEntity(Match successMatch) {

        MatchDto matchDto = new MatchDto();
        matchDto.setQueueId(successMatch.getQueueId());
        matchDto.setSeasonId(successMatch.getSeasonId());
        matchDto.setVersion(successMatch.getVersion());
        matchDto.setGameDuration(successMatch.getGameDuration());
        matchDto.setGameEndTimestamp(successMatch.getGameEndTimestamp());

        if(successMatch.getTeams() == null){
            return matchDto;
        }
        for(Team team : successMatch.getTeams()){

            TeamDto teamDto = new TeamDto();
            teamDto.setTeamId(team.getTeamPositionId());
            teamDto.setGameResult(team.getGameResult());
            matchDto.getTeams().add(teamDto);

            if(team.getMembers() == null){
                return matchDto;
            }
            for(SummaryMember summaryMember : team.getMembers()){

                ParticipantDto participantDto = new ParticipantDto();
                participantDto.setSummonerId(summaryMember.getSummonerId());
                participantDto.setBanChampionId(summaryMember.getBanChampionId());
                participantDto.setPickChampionId(summaryMember.getPickChampionId());
                participantDto.setPositionId(summaryMember.getPositionId());
                participantDto.setChampionLevel(summaryMember.getChampionLevel());
                participantDto.setMinionKills(summaryMember.getMinionKills());

                participantDto.setKills(summaryMember.getKills());
                participantDto.setDeaths(summaryMember.getDeaths());
                participantDto.setAssists(summaryMember.getAssists());

                participantDto.setItem0(summaryMember.getItem0());
                participantDto.setItem1(summaryMember.getItem1());
                participantDto.setItem2(summaryMember.getItem2());
                participantDto.setItem3(summaryMember.getItem3());
                participantDto.setItem4(summaryMember.getItem4());
                participantDto.setItem5(summaryMember.getItem5());
                participantDto.setItem6(summaryMember.getItem6());

                teamDto.getBanList().add(summaryMember.getBanChampionId());
                teamDto.getParticipantDtoList().add(participantDto);

                DetailMember detailMember = summaryMember.getDetailMember();
                if(detailMember == null){
                    return matchDto;
                }
                ParticipantDetailDto detailDto = new ParticipantDetailDto();
                detailDto.setGoldEarned(detailMember.getGoldEarned());
                detailDto.setGoldSpent(detailMember.getGoldSpent());
                detailDto.setTotalDamageDealt(detailMember.getTotalDamageDealt());
                detailDto.setTotalDamageDealtToChampions(detailMember.getTotalDamageDealtToChampions());
                detailDto.setTotalDamageShieldedOnTeammates(detailMember.getTotalDamageShieldedOnTeammates());
                detailDto.setTotalDamageTaken(detailMember.getTotalDamageTaken());
                detailDto.setTimeCCingOthers(detailMember.getTimeCCingOthers());
                detailDto.setTotalHeal(detailMember.getTotalHeal());
                detailDto.setTotalHealsOnTeammates(detailMember.getTotalHealsOnTeammates());
                detailDto.setDetectorWardPurchased(detailMember.getDetectorWardPurchased());
                detailDto.setDetectorWardsPlaced(detailMember.getDetectorWardsPlaced());
                detailDto.setWardKills(detailMember.getWardKills());
                detailDto.setWardsPlaced(detailMember.getWardsPlaced());

                participantDto.setDetailDto(detailDto);

                Perks perks = summaryMember.getPerks();
                if(perks == null){
                    return matchDto;
                }
                PerksDto perksDto = new PerksDto();
                perksDto.setMainPerkStyle(perks.getMainPerkStyle());
                perksDto.setSubPerkStyle(perks.getSubPerkStyle());
                perksDto.setMainPerk1(perks.getMainPerk1());
                perksDto.setMainPerk2(perks.getMainPerk2());
                perksDto.setMainPerk3(perks.getMainPerk3());
                perksDto.setMainPerk4(perks.getMainPerk4());
                perksDto.setSubPerk1(perks.getSubPerk1());
                perksDto.setSubPerk2(perks.getSubPerk2());

                detailDto.setPerksDto(perksDto);
                participantDto.setMainPerk1(summaryMember.getPerks().getMainPerk1());
                participantDto.setSubPerkStyle(summaryMember.getPerks().getSubPerkStyle());

                PerkStats perkStats = summaryMember.getPerks().getPerkStats();
                if(perkStats == null){
                    return matchDto;
                }
                PerkStatsDto perkStatsDto = new PerkStatsDto();
                perkStatsDto.setDefense(perkStats.getDefense());
                perkStatsDto.setFlex(perkStats.getFlex());
                perkStatsDto.setOffense(perkStats.getOffense());

                detailDto.setPerkStatsDto(perkStatsDto);
            }
        }

        return  matchDto;
    }

}
