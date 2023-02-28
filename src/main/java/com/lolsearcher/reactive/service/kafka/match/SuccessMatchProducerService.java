package com.lolsearcher.reactive.service.kafka.match;

import com.lolsearcher.reactive.model.entity.match.Match;
import com.lolsearcher.reactive.service.kafka.RecordProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.SUCCESS_MATCH_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Service
public class SuccessMatchProducerService implements RecordProducerService<String, Match> {

    private final ReactiveKafkaProducerTemplate<String, Match> successMatchTemplate;

    @Override
    public Mono<Void> sendRecord(String key, Match record) {

        return successMatchTemplate.send(SUCCESS_MATCH_TOPIC, key, record)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("Key : '{}', Value : '{}' 데이터가 카프카에 저장 성공", key, record.getMatchId());
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("Key : '{}', Value : '{}' 데이터가 카프카에 저장 실패", key, record.getMatchId());
                    return Mono.error(new RuntimeException(e));
                });
    }

    @Override
    public Mono<Void> sendRecord(Match record) {

        return successMatchTemplate.send(SUCCESS_MATCH_TOPIC, record)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("Value : '{}' 데이터 카프카에 저장 성공", record.getMatchId());
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("Value : '{}' 데이터 카프카에 저장 실패", record.getMatchId());
                    return Mono.error(new RuntimeException(e));
                });
    }
}
