package com.parkson.utils.kafka.util.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

public interface KafkaListenerService {
    @KafkaListener(topics =  "#{kafkaTopicName}", groupId = "#{topicGroupId}")
    public void processMessage(ConsumerRecord<Integer,String> record);
}
