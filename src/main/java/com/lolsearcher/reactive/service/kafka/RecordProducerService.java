package com.lolsearcher.reactive.service.kafka;

import reactor.core.publisher.Mono;

public interface RecordProducerService<K, V> {

    Mono<Void> sendRecord(K key, V record);
    Mono<Void> sendRecord(V record);
}
