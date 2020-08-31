package com.parkson.utils.kafka.util.annotation;


import com.parkson.utils.kafka.util.config.KafkaTopicConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({KafkaTopicConfiguration.class})
public @interface EnableUtilsKafka {
}
