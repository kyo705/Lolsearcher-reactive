package com.lolsearcher.reactive.service.search.summoner;

import com.lolsearcher.reactive.api.RiotGamesApi;
import com.lolsearcher.reactive.model.factory.EntityFactory;
import com.lolsearcher.reactive.model.factory.ResponseFactory;
import com.lolsearcher.reactive.model.input.front.RequestSummonerDto;
import com.lolsearcher.reactive.model.input.front.RequestUpdatingSummonerDto;
import com.lolsearcher.reactive.model.output.summoner.SummonerDto;
import com.lolsearcher.reactive.service.kafka.summoner.SummonerProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SummonerService {

    private final SummonerProducerService summonerProducerService;
    private final RiotGamesApi riotGamesApi;

    public Mono<SummonerDto> getRenewSummoner(RequestSummonerDto requestSummonerDto) {

        String name = requestSummonerDto.getSummonerName();

        return riotGamesApi.getSummonerByName(name)
                .map(EntityFactory::getSummonerFromApiDto)
                .doOnNext(summonerProducerService::sendRecord)
                .map(ResponseFactory::getSummonerDtoFromEntity);
    }

    public Flux<SummonerDto> updateSameNameSummoners(RequestUpdatingSummonerDto request){

        String realSummonerName = request.getRealSummonerName();
        List<String> summonerIds = request.getSummonerIds();

        return Flux.fromStream(summonerIds.stream())
                .flatMap(riotGamesApi::getSummonerById)
                .map(EntityFactory::getSummonerFromApiDto)
                .doOnNext(summonerProducerService::sendRecord)
                .filter(summoner -> summoner.getSummonerName().equals(realSummonerName))
                .map(ResponseFactory::getSummonerDtoFromEntity);
    }
}
