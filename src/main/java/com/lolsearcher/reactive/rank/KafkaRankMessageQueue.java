package com.lolsearcher.reactive.rank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.rank.RankConstant.RANK_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaRankMessageQueue implements RankMessageQueue {

    private final ReactiveKafkaProducerTemplate<String, RankDto> kafkaTemplate;

    @Override
    public Mono<Void> send(String key, RankDto rank) {

        return kafkaTemplate.send(RANK_TOPIC, key, rank)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("Key : '{}', Value : '{}' 데이터가 카프카에 저장 성공", key, rank);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("Key : '{}', Value : '{}' 데이터가 카프카에 저장 실패", key, rank);
                    return Mono.error(new RuntimeException(e));
                });
    }

    @Override
    public Mono<Void> send(RankDto rank) {

        return kafkaTemplate.send(RANK_TOPIC, rank)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("Value : '{}' 데이터가 카프카에 저장 성공", rank);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("Value : '{}' 데이터가 카프카에 저장 실패", rank);
                    return Mono.error(new RuntimeException(e));
                });
    }
}
