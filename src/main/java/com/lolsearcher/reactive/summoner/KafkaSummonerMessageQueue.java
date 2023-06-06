package com.lolsearcher.reactive.summoner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaSummonerMessageQueue implements SummonerMessageQueue {

    private final ReactiveKafkaProducerTemplate<String, SummonerDto> kafkaTemplate;

    @Override
    public Mono<Void> send(String key, SummonerDto summoner) {

        return kafkaTemplate.send(SUMMONER_TOPIC, key, summoner)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("Key : '{}', Value : '{}' 데이터가 카프카에 저장 성공", key, summoner);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("Key : '{}', Value : '{}' 데이터가 카프카에 저장 실패", key, summoner);
                    return Mono.error(new RuntimeException(e));
                });
    }

    @Override
    public Mono<Void> send(SummonerDto summoner) {

        return kafkaTemplate.send(SUMMONER_TOPIC, summoner)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("Value : '{}' 데이터가 카프카에 저장 성공", summoner);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("Value : '{}' 데이터가 카프카에 저장 실패", summoner);
                    return Mono.error(new RuntimeException(e));
                });
    }
}
