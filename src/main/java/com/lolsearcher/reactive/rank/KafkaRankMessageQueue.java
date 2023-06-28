package com.lolsearcher.reactive.rank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.lolsearcher.reactive.config.ReactiveKafkaProducerConfig.MQ_THREAD_PREFIX;
import static com.lolsearcher.reactive.rank.RankConstant.RANK_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaRankMessageQueue implements RankMessageQueue {

    private final ReactiveKafkaProducerTemplate<String, RankDto> kafkaTemplate;

    @Override
    public void send(String key, RankDto rank) {

        kafkaTemplate.send(RANK_TOPIC, key, rank)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, rank);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Key : '{}', Value : '{}'", key, rank);
                    return Mono.error(new RuntimeException(e));
                })
                .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                .subscribe();
    }

    @Override
    public void send(RankDto rank) {

        kafkaTemplate.send(RANK_TOPIC, rank)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Value : '{}'", rank);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Value : '{}'", rank);
                    return Mono.error(new RuntimeException(e));
                })
                .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                .subscribe();
    }
}
