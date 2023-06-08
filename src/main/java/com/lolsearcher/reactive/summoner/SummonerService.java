package com.lolsearcher.reactive.summoner;

import com.lolsearcher.reactive.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SummonerService {

    private final SummonerMessageQueue summonerMessageQueue;
    private final SummonerAPI summonerAPI;

    public Mono<SummonerDto> findByName(String name) {

        return summonerAPI.findByName(name)
                .map(ResponseFactory::getSummonerDto)
                .doOnNext(summonerMessageQueue::send);
    }

    public Flux<SummonerDto> updateSameNameSummoners(String realSummonerName, SummonerUpdateRequest request){

        List<String> summonerIds = request.getSummonerIds();

        return Flux.fromStream(summonerIds.stream())
                .flatMap(summonerAPI::findById)
                .map(ResponseFactory::getSummonerDto)
                .doOnNext(summonerMessageQueue::send)
                .filter(summoner -> summoner.getSummonerName().equals(realSummonerName));
    }
}
