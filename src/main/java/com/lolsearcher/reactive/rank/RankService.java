package com.lolsearcher.reactive.rank;

import com.lolsearcher.reactive.errors.exception.IncorrectSummonerRankSizeException;
import com.lolsearcher.reactive.errors.exception.NonUniqueRankTypeException;
import com.lolsearcher.reactive.utils.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.lolsearcher.reactive.rank.RankConstant.THE_NUMBER_OF_RANK_TYPE;

@RequiredArgsConstructor
@Service
public class RankService {

    private final RankMessageQueue rankMessageQueue;
    private final RankAPI rankAPI;

    public Mono<Map<RankTypeState, RankDto>> renewBySummonerId(String summonerId) {

        Set<RankTypeState> set = new HashSet<>();

        return rankAPI.findAllBySummonerId(summonerId)
                .map(ResponseFactory::getRankDto)
                .doOnNext(rank -> {
                    if(set.contains(rank.getQueueType())) {
                        throw new NonUniqueRankTypeException(rank.getQueueType());
                    }
                    set.add(rank.getQueueType());
                })
                .collectMap(RankDto::getQueueType, rank -> rank)
                .doOnNext(map -> {
                    if(map.size() > THE_NUMBER_OF_RANK_TYPE) {
                        throw new IncorrectSummonerRankSizeException(map.size());
                    }
                })
                .doOnNext(map -> map.values().stream().peek(rankMessageQueue::send));
    }
}
