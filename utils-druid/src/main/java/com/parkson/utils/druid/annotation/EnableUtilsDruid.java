package com.parkson.utils.druid.annotation;

import com.parkson.utils.druid.config.DruidConfig;
import com.parkson.utils.druid.config.DruidProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName EnableUtilsDruid
 * @Description TO DO
 * @Author cheneason
 * @Date 2020/7/30 09:41
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ DruidConfig.class, DruidProperties.class })
public @interface EnableUtilsDruid {
}
