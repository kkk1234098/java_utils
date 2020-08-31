package com.parkson.utils.kafka.util.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Lazy
@Component
public class KafkaUtil {
    private Logger log = LoggerFactory.getLogger(KafkaUtil.class);
    private final KafkaTemplate<Integer,String> kafkaTemplate;

    @Autowired
    public KafkaUtil(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(String topic,String data){
        log.info("kafka sendMessage start");
        ListenableFuture<SendResult<Integer,String>> future = kafkaTemplate.send(topic,data);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("kafka sendMessage error,ex = {},topic = {},data = {}",ex,topic,data);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> integerStringSendResult) {
                log.info("kafka sendMessage success topic = {},data = {}",topic,data);
            }
        });
        log.info("kafka sendMessage end");
    }

}
