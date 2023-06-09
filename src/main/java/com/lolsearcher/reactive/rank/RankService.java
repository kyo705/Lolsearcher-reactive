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

    public Mono<Map<RankTypeState, RankDto>> findAll(String summonerId) {

        Set<String> set = new HashSet<>();

        return rankAPI.findAll(summonerId)
                .flatMap(rank -> {
                    if(set.contains(rank.getQueueType())) {
                        return Mono.error(new NonUniqueRankTypeException(rank.getQueueType()));
                    }
                    set.add(rank.getQueueType());

                    if(set.size() > THE_NUMBER_OF_RANK_TYPE) {
                        return Mono.error(new IncorrectSummonerRankSizeException(set.size()));
                    }
                    return Mono.just(ResponseFactory.getRankDto(rank));
                })
                .collectMap(RankDto::getQueueType, rank -> rank)
                .doOnSuccess(map -> map.values().forEach(rankMessageQueue::send));
    }
}
