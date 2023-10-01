package com.lolsearcher.reactive.summoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ReactiveKafkaProducerTemplate<String, String> kafkaTemplate;

    @Override
    public void send(String key, SummonerDto summoner) {

        try {
            String record = objectMapper.writeValueAsString(summoner);

            kafkaTemplate.send(SUMMONER_TOPIC, key, record)
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
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(SummonerDto summoner) {

        try {
            String record = objectMapper.writeValueAsString(summoner);

            kafkaTemplate.send(SUMMONER_TOPIC, record)
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
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
