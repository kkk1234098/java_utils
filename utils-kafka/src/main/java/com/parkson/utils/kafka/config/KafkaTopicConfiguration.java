package com.parkson.utils.kafka.config;

import com.parkson.utils.kafka.util.KafkaUtil;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * @ClassName KafkaTopicConfiguration
 * @Description TO DO
 * @Author hc
 * @Date 2020/8/31
 * @Version 1.0
 */

@Data
@Component
@ConfigurationProperties("kafka.topic")
@Configuration
@PropertySource("classpath:kafka.properties")
public class KafkaTopicConfiguration implements Serializable {
    private static Logger log = LoggerFactory.getLogger(KafkaTopicConfiguration.class);

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.producer.retries}")
    private Integer producerRetries;

    @Value("${kafka.producer.batch-size}")
    private Integer producerBatchSize;

    @Value("${kafka.producer.buffer-memory}")
    private Integer producerBufferMemory;

    @Value("${kafka.producer.key-serializer}")
    private String producerKeySerializer;

    @Value("${kafka.producer.value-serializer}")
    private String producerValueSerializer;

    @Value("${kafka.producer.properties.linger.ms}")
    private String producerPropertiesLingerMs;

    @Value("${kafka.consumer.enable-auto-commit}")
    private Boolean consumerEnableAutoCommit;

    @Value("${kafka.consumer.auto-commit-interval}")
    private String consumerAutoCommitInterval;

    @Value("${kafka.consumer.key-deserializer}")
    private String consumerKeyDeserializer;

    @Value("${kafka.consumer.value-deserializer}")
    private String consumerValueDeserializer;

    @Value("${kafka.consumer.properties.session.timeout.ms}")
    private Integer consumerPropertiesSessionTimeoutMs;

    private String groupId;
    private String[] topicName;

//    private final KafkaTopicProperties properties;
//
//    public KafkaTopicConfiguration(@Qualifier("kafkaTopicProperties") KafkaTopicProperties properties) {
//        this.properties = properties;
//    }


    @Bean
    public String[] kafkaTopicName(){
//        return properties.getTopicName();
        return this.getTopicName();
    }

    @Bean
    public String topicGroupId(){
//        return properties.getGroupId();
        return this.getGroupId();
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        log.info("初始化kafka消费者");
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.consumerKeyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.consumerValueDeserializer);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,this.consumerEnableAutoCommit);
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,this.consumerAutoCommitInterval);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,this.consumerPropertiesSessionTimeoutMs);
        return props;
    }



    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.producerKeySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.producerValueSerializer);
        props.put(ProducerConfig.RETRIES_CONFIG,this.producerRetries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,this.producerBatchSize);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,this.producerBufferMemory);
        props.put(ProducerConfig.LINGER_MS_CONFIG,this.producerPropertiesLingerMs);
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        log.info("初始化kafkaTemplate");
        return new KafkaTemplate<String, String>(producerFactory());
    }
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        log.info("初始化kafka提供者");
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }



    @Bean(name = "kafkaUtil")
    public KafkaUtil kafkaUtil(KafkaTemplate kafkaTemplate) {
        log.info("KafkaUtil注入成功！");
        KafkaUtil kafkaUtil = new KafkaUtil(kafkaTemplate);
        return kafkaUtil;
    }

}
