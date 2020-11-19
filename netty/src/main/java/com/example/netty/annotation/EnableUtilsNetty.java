package com.example.netty.annotation;

import com.example.netty.config.NettyConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName EnableUtilsKafka
 * @Description TO DO
 * @Author hc
 * @Date 2020/11/19
 * @Version 1.0
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({NettyConfig.class})
public @interface EnableUtilsNetty {
}
