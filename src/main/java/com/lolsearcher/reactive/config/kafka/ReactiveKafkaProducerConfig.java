package com.lolsearcher.reactive.config.kafka;

import com.lolsearcher.reactive.model.entity.match.Match;
import com.lolsearcher.reactive.model.entity.rank.Rank;
import com.lolsearcher.reactive.model.entity.summoner.Summoner;
import com.lolsearcher.reactive.model.input.kafka.RemainMatchIdRange;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.*;

@Configuration
public class ReactiveKafkaProducerConfig {

    @Qualifier(SUMMONER_TEMPLATE)
    @Bean
    public ReactiveKafkaProducerTemplate<String, Summoner> summonerTemplate(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.ZSTD.name);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Qualifier(RANK_TEMPLATE)
    @Bean
    public ReactiveKafkaProducerTemplate<String, Rank> rankTemplate(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.ZSTD.name);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Qualifier(FAIL_MATCH_ID_TEMPLATE)
    @Bean
    public ReactiveKafkaProducerTemplate<String, String> failMatchIdTemplate(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Qualifier(SUCCESS_MATCH_TEMPLATE)
    @Bean
    public ReactiveKafkaProducerTemplate<String, Match> successMatchTemplate(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.ZSTD.name);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Qualifier(REMAIN_MATCH_ID_RANGE_TEMPLATE)
    @Bean
    public ReactiveKafkaProducerTemplate<String, RemainMatchIdRange> remainMatchIdRangeTemplate(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.ZSTD.name);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Qualifier(LAST_MATCH_ID_TEMPLATE)
    @Bean
    public ReactiveKafkaProducerTemplate<String, String> lastMatchIdTemplate(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }
}
