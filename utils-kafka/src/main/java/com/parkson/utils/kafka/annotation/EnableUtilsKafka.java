package com.parkson.utils.kafka.annotation;


import com.parkson.utils.kafka.config.KafkaTopicConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName EnableUtilsKafka
 * @Description TO DO
 * @Author hc
 * @Date 2020/8/31
 * @Version 1.0
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({KafkaTopicConfiguration.class})
public @interface EnableUtilsKafka {
}
