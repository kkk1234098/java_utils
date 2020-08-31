package com.parkson.utils.redis.annotation;

import com.parkson.utils.redis.config.RedisConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName EnableUtilsRedis
 * @Description TO DO
 * @Author chenyijun
 * @Date 2020/8/24 4:44 下午
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({RedisConfig.class})
public @interface EnableUtilsRedis {
}
