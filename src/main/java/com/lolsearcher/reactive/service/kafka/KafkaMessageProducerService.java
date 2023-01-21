package com.lolsearcher.reactive.service.kafka;

import com.lolsearcher.reactive.model.entity.match.Match;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.lolsearcher.reactive.constant.KafkaConstant.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaMessageProducerService {

    private final Map<String, ReactiveKafkaProducerTemplate> kafkaProducerTemplates;

    public void sendFailMatchId(String failMatchId) {

        ((ReactiveKafkaProducerTemplate<String, String>) kafkaProducerTemplates.get(FAIL_MATCH_ID_PRODUCER_TEMPLATE))
                .send(FAIL_MATCH_ID_TOPIC, failMatchId)
                .doOnError(e->{
                    // 1. 관리자에게 메일 전송 => 2. 파일 시스템에 저장
                    log.error(e.getMessage());
                    log.error("카프카에 '{}' 데이터 저장 실패", failMatchId);
                })
                .subscribe();
    }

    public void sendSuccessMatch(Match match) {

        ((ReactiveKafkaProducerTemplate<String, Match>)kafkaProducerTemplates.get(SUCCESS_MATCH_PRODUCER_TEMPLATE))
                .send(SUCCESS_MATCH_TOPIC, match)
                .doOnError(e->{
                    // 1. 관리자에게 메일 전송 => 2. 파일 시스템에 저장
                    log.error(e.getMessage());
                    log.error("카프카에 '{}' 데이터 저장 실패", match);
                })
                .subscribe();
    }
}
