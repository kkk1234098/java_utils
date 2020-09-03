package com.parkson.utils.kafka.util.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @ClassName KafkaListenerService
 * @Description TO DO
 * @Author hc
 * @Date 2020/8/31
 * @Version 1.0
 */

public interface KafkaListenerService {
    @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}")
    public void processMessage(ConsumerRecord<Integer, String> record);
}
