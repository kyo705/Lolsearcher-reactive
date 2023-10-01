package com.lolsearcher.reactive.match;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearcher.reactive.match.dto.MatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.lolsearcher.reactive.config.ReactiveKafkaProducerConfig.MQ_THREAD_PREFIX;
import static com.lolsearcher.reactive.match.MatchConstant.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaMatchMessageQueue implements MatchMessageQueue {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ReactiveKafkaProducerTemplate<String, String> kafkaTemplate;

    @Override
    public void sendFailMatchId(String failMatchId) {

        kafkaTemplate.send(FAIL_MATCH_ID_TOPIC, failMatchId)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Value : '{}'", failMatchId);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Value : '{}'", failMatchId);
                    return Mono.error(new RuntimeException(e));
                })
                .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                .subscribe();
    }

    @Override
    public void sendFailMatchId(String key, String failMatchId) {

        kafkaTemplate.send(FAIL_MATCH_ID_TOPIC, key, failMatchId)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, failMatchId);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Key : '{}', Value : '{}'", key, failMatchId);
                    return Mono.error(new RuntimeException(e));
                })
                .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                .subscribe();
    }

    @Override
    public void sendSuccessMatch(MatchDto successMatch) {

        try {
            String record = objectMapper.writeValueAsString(successMatch);

            kafkaTemplate.send(SUCCESS_MATCH_TOPIC, record)
                    .flatMap(result -> {
                        Exception e = result.exception();

                        if(e == null){
                            log.info("카프카에 저장 성공!! Value : '{}'", successMatch);
                            return Mono.empty();
                        }
                        log.error(e.getMessage());
                        log.error("카프카에 저장 실패... Value : '{}'", successMatch);
                        return Mono.error(new RuntimeException(e));
                    })
                    .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                    .subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendSuccessMatch(String key, MatchDto successMatch) {

        try {
            String record = objectMapper.writeValueAsString(successMatch);

            kafkaTemplate.send(SUCCESS_MATCH_TOPIC, key, record)
                    .flatMap(result -> {
                        Exception e = result.exception();

                        if(e == null){
                            log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, successMatch);
                            return Mono.empty();
                        }
                        log.error(e.getMessage());
                        log.error("카프카에 저장 실패... Key : '{}', Value : '{}'", key, successMatch);
                        return Mono.error(new RuntimeException(e));
                    })
                    .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                    .subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendRemainMatchIds(String key, RemainMatchIdRange remainMatchIdRange) {

        try {
            String record = objectMapper.writeValueAsString(remainMatchIdRange);

            kafkaTemplate.send(REMAIN_MATCH_ID_RANGE_TOPIC, key, record)
                    .flatMap(result -> {
                        Exception e = result.exception();

                        if(e == null){
                            log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, remainMatchIdRange);
                            return Mono.empty();
                        }
                        log.error(e.getMessage());
                        log.error("카프카에 저장 실패... Key : '{}', Value : '{}'", key, remainMatchIdRange);
                        return Mono.error(new RuntimeException(e));
                    })
                    .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                    .subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void sendLastMatchId(String key, String lastMatchId) {


        kafkaTemplate.send(LAST_MATCH_ID_TOPIC, key, lastMatchId)
                .flatMap(result -> {
                    Exception e = result.exception();

                    if(e == null){
                        log.info("카프카에 저장 성공!! Key : '{}', Value : '{}'", key, lastMatchId);
                        return Mono.empty();
                    }
                    log.error(e.getMessage());
                    log.error("카프카에 저장 실패... Key : '{}', Value : '{}'", key, lastMatchId);
                    return Mono.error(new RuntimeException(e));
                })
                .subscribeOn(Schedulers.newParallel(MQ_THREAD_PREFIX))
                .subscribe();
    }
}
