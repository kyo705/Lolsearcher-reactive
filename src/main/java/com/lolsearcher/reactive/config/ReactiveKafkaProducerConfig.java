package com.lolsearcher.reactive.config;

import com.lolsearcher.reactive.match.RemainMatchIdRange;
import com.lolsearcher.reactive.match.dto.MatchDto;
import com.lolsearcher.reactive.rank.RankDto;
import com.lolsearcher.reactive.summoner.SummonerDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

import static com.lolsearcher.reactive.match.MatchConstant.*;
import static com.lolsearcher.reactive.rank.RankConstant.RANK_TEMPLATE;
import static com.lolsearcher.reactive.summoner.SummonerConstant.SUMMONER_TEMPLATE;

@Configuration
public class ReactiveKafkaProducerConfig {

    @Value("${lolsearcher.kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;

    @Qualifier(SUMMONER_TEMPLATE)
    @Bean
    public ReactiveKafkaProducerTemplate<String, SummonerDto> summonerTemplate(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.ZSTD.name);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Qualifier(RANK_TEMPLATE)
    @Bean
    public ReactiveKafkaProducerTemplate<String, RankDto> rankTemplate(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
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

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

    @Qualifier(SUCCESS_MATCH_TEMPLATE)
    @Bean
    public ReactiveKafkaProducerTemplate<String, MatchDto> successMatchTemplate(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
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

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }
}
