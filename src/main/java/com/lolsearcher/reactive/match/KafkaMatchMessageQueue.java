package com.lolsearcher.reactive.match;

import com.lolsearcher.reactive.match.dto.MatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.lolsearcher.reactive.match.MatchConstant.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaMatchMessageQueue implements MatchMessageQueue {

    private final Map<String, ReactiveKafkaProducerTemplate> kafkaTemplates;

    @Override
    public Mono<Void> sendFailMatchId(String failMatchId) {

        return ((ReactiveKafkaProducerTemplate<String, String>)kafkaTemplates.get(FAIL_MATCH_ID_TEMPLATE))
                .send(FAIL_MATCH_ID_TOPIC, failMatchId)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Value : '{}'", failMatchId);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Value : '{}'", failMatchId);
                    return Mono.error(new RuntimeException(e));
                });
    }

    @Override
    public Mono<Void> sendFailMatchId(String key, String failMatchId) {

        return ((ReactiveKafkaProducerTemplate<String, String>)kafkaTemplates.get(FAIL_MATCH_ID_TEMPLATE))
                .send(FAIL_MATCH_ID_TOPIC, key, failMatchId)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, failMatchId);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Key : '{}', Value : '{}'", key, failMatchId);
                    return Mono.error(new RuntimeException(e));
                });
    }

    @Override
    public Mono<Void> sendSuccessMatch(MatchDto successMatch) {

        return ((ReactiveKafkaProducerTemplate<String, MatchDto>)kafkaTemplates.get(SUCCESS_MATCH_TEMPLATE))
                .send(SUCCESS_MATCH_TOPIC, successMatch)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Value : '{}'", successMatch);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Value : '{}'", successMatch);
                    return Mono.error(new RuntimeException(e));
                });
    }

    @Override
    public Mono<Void> sendSuccessMatch(String key, MatchDto successMatch) {

        return ((ReactiveKafkaProducerTemplate<String, MatchDto>)kafkaTemplates.get(SUCCESS_MATCH_TEMPLATE))
                .send(SUCCESS_MATCH_TOPIC, key, successMatch)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, successMatch);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Key : '{}', Value : '{}'", key, successMatch);
                    return Mono.error(new RuntimeException(e));
                });
    }

    @Override
    public Mono<Void> sendRemainMatchIds(String key, RemainMatchIdRange remainMatchIdRange) {

        return ((ReactiveKafkaProducerTemplate<String, RemainMatchIdRange>)kafkaTemplates.get(REMAIN_MATCH_ID_RANGE_TEMPLATE))
                .send(REMAIN_MATCH_ID_RANGE_TOPIC, key, remainMatchIdRange)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, remainMatchIdRange);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Key : '{}', Value : '{}'", key, remainMatchIdRange);
                    return Mono.error(new RuntimeException(e));
                });
    }

    @Override
    public Mono<Void> sendLastMatchId(String key, String lastMatchId) {

        return ((ReactiveKafkaProducerTemplate<String, String>)kafkaTemplates.get(LAST_MATCH_ID_TEMPLATE))
                .send(LAST_MATCH_ID_TOPIC, key, lastMatchId)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, lastMatchId);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Key : '{}', Value : '{}'", key, lastMatchId);
                    return Mono.error(new RuntimeException(e));
                });
    }
}
