package com.lolsearcher.reactive.service.kafka.summoner;

import com.lolsearcher.reactive.model.entity.summoner.Summoner;
import com.lolsearcher.reactive.service.kafka.RecordProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.SUMMONER_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Service
public class SummonerProducerService implements RecordProducerService<String, Summoner> {

    private final ReactiveKafkaProducerTemplate<String, Summoner> summonerTemplate;

    @Override
    public Mono<Void> sendRecord(String key, Summoner record) {

        return summonerTemplate.send(SUMMONER_TOPIC, key, record)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("Key : '{}', Value : '{}' 데이터가 카프카에 저장 성공", key, record);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("Key : '{}', Value : '{}' 데이터가 카프카에 저장 실패", key, record);
                    return Mono.error(new RuntimeException(e));
                });
    }

    @Override
    public Mono<Void> sendRecord(Summoner record) {

        return summonerTemplate.send(SUMMONER_TOPIC, record)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("Value : '{}' 데이터가 카프카에 저장 성공", record);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("Value : '{}' 데이터가 카프카에 저장 실패", record);
                    return Mono.error(new RuntimeException(e));
                });
    }
}
