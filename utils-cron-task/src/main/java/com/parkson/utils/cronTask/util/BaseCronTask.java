package com.parkson.utils.cronTask.util;

import com.parkson.utils.core.util.SpringContextHolder;
import com.parkson.utils.cronTask.config.ScheduleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @ClassName BaseCronTask
 * @Description TO DO
 * @Author chenyijun
 * @Date 2020/10/28 4:30 下午
 * @Version 1.0
 */
public class BaseCronTask {

    private static Logger log = LoggerFactory.getLogger(BaseCronTask.class);

    // 保存任务
    public Map<String, ScheduledFuture<?>> futuresMap = new ConcurrentHashMap<String, ScheduledFuture<?>>();

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(10);
        return threadPoolTaskScheduler;
    }

    /**
     * 添加任务
     *
     * @param s
     */
    public void addTask(ScheduleConfig s) {
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(getRunnable(s), getTrigger(s));
        futuresMap.put(s.getJobName(), future);
    }

    /**
     * 暂停任务
     *
     * @param key
     * @return
     */
    public boolean pauseTask(String key) {
        ScheduledFuture toBeRemovedFuture = futuresMap.remove(key);
        if (toBeRemovedFuture != null) {
            toBeRemovedFuture.cancel(true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新任务
     *
     * @param s
     */
    public void updateTask(ScheduleConfig s) {
        ScheduledFuture toBeRemovedFuture = futuresMap.remove(s.getJobName());
        if (toBeRemovedFuture != null) {
            toBeRemovedFuture.cancel(true);
        }
        addTask(s);
    }

    /**
     * runnable
     *
     * @param scheduleConfig
     * @return
     */
    public Runnable getRunnable(ScheduleConfig scheduleConfig) {
        return new Runnable() {
            @Override
            public void run() {
                Class<?> clazz;
                try {
                    clazz = Class.forName(scheduleConfig.getClassName());
                    String className = lowerFirstCase(clazz.getSimpleName());
                    Object bean = (Object) SpringContextHolder.getBean(className);
                    Method method = ReflectionUtils.findMethod(bean.getClass(), scheduleConfig.getMethod());
                    log.info("定时任务开始执行：{}, Method: {}", scheduleConfig.getJobName(), scheduleConfig.getMethod());
                    ReflectionUtils.invokeMethod(method, bean);
                    log.info("定时任务执行完毕：{}, Method: {}", scheduleConfig.getJobName(), scheduleConfig.getMethod());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Trigger
     *
     * @param scheduleConfig
     * @return
     */
    public Trigger getTrigger(ScheduleConfig scheduleConfig) {
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(scheduleConfig.getCron());
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        };
    }

    /**
     * 转换首字母小写
     *
     * @param str
     * @return
     */
    public static String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
