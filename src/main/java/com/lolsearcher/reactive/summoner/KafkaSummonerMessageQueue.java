package com.lolsearcher.reactive.summoner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.lolsearcher.reactive.config.ReactiveKafkaProducerConfig.MQ_THREAD_PREFIX;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaSummonerMessageQueue implements SummonerMessageQueue {

    private final ReactiveKafkaProducerTemplate<String, SummonerDto> kafkaTemplate;

    @Override
    public void send(String key, SummonerDto summoner) {

        kafkaTemplate.send(SUMMONER_TOPIC, key, summoner)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, summoner);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Key : '{}', Value : '{}' ", key, summoner);
                    return Mono.error(new RuntimeException(e));
                })
                .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                .subscribe();
    }

    @Override
    public void send(SummonerDto summoner) {

        kafkaTemplate.send(SUMMONER_TOPIC, summoner)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Value : '{}'", summoner);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Value : '{}'", summoner);
                    return Mono.error(new RuntimeException(e));
                })
                .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                .subscribe();
    }
}
