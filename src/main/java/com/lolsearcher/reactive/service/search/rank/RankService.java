package com.lolsearcher.reactive.service.search.rank;

import com.lolsearcher.reactive.api.RiotGamesApi;
import com.lolsearcher.reactive.model.entity.rank.Rank;
import com.lolsearcher.reactive.model.factory.EntityFactory;
import com.lolsearcher.reactive.model.factory.ResponseFactory;
import com.lolsearcher.reactive.model.input.front.RequestRankDto;
import com.lolsearcher.reactive.model.output.rank.RankDto;
import com.lolsearcher.reactive.service.kafka.rank.RankProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class RankService {

    private final RankProducerService rankProducerService;
    private final RiotGamesApi riotGamesApi;

    public Mono<Map<String, RankDto>> getRankDto(RequestRankDto requestRankDto) {

        String summonerId = requestRankDto.getSummonerId();

        return riotGamesApi.getRank(summonerId)
                .map(EntityFactory::getRankFromApiDto)
                .doOnNext(rankProducerService::sendRecord)
                .collectMap(Rank::getQueueType, ResponseFactory::getRankDtoFromEntity);
    }
}
