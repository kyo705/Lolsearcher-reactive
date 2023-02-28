package com.lolsearcher.reactive.service.kafka.match;

import com.lolsearcher.reactive.exception.exception.InvalidMethodCallException;
import com.lolsearcher.reactive.model.input.kafka.RemainMatchIdRange;
import com.lolsearcher.reactive.service.kafka.RecordProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.REMAIN_MATCH_ID_RANGE_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Service
public class RemainMatchIdRangeProducerService implements RecordProducerService<String, RemainMatchIdRange> {

    private final ReactiveKafkaProducerTemplate<String, RemainMatchIdRange> remainMatchIdRangeTemplate;

    @Override
    public Mono<Void> sendRecord(String key, RemainMatchIdRange record) {
        return remainMatchIdRangeTemplate.send(REMAIN_MATCH_ID_RANGE_TOPIC, key, record)
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
    public Mono<Void> sendRecord(RemainMatchIdRange record) {
        log.error("KEY 값으로 반드시 SummonerId 값이 있어야 함");
        return Mono.error(new InvalidMethodCallException());
    }
}
