package com.lolsearcher.reactive.integration.service.kafka;

import com.lolsearcher.reactive.model.entity.match.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static com.lolsearcher.reactive.constant.constant.KafkaConstant.*;

public class KafkaServiceTestSetup {

    protected static Match getMatch() throws IllegalAccessException {

        Match match = new Match();
        match.setMatchId("matchId1");

        Team team = new Team();
        team.setMatch(match);

        SummaryMember summaryMember = new SummaryMember();
        summaryMember.setTeam(team);

        DetailMember detailMember = new DetailMember();
        detailMember.setSummaryMember(summaryMember);

        PerkStats perkStats = new PerkStats();

        Perks perks = new Perks();
        perks.setSummaryMember(summaryMember);
        perks.setPerkStats(perkStats);

        return match;
    }

    protected static ConsumerRecords<String, String> consumeFailMatchIdRecord() {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "fail_match_id_group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(List.of(FAIL_MATCH_ID_TOPIC));
        return kafkaConsumer.poll(Duration.ofSeconds(5));
    }

    public static ConsumerRecords<String, Match> consumeSuccessMatchRecord() {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "success_match_group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.lolsearcher.reactive.model.entity.match");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, Match> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(List.of(SUCCESS_MATCH_TOPIC));
        return kafkaConsumer.poll(Duration.ofSeconds(5));
    }
}
