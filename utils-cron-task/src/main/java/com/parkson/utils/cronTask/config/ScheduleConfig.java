package com.parkson.utils.cronTask.config;

import java.util.Date;

/**
 * @ClassName ScheduleConfig
 * @Description TO DO
 * @Author chenyijun
 * @Date 2020/10/28 4:29 下午
 * @Version 1.0
 */
public class ScheduleConfig {

    private Long id;

    // 任务名字
    private String jobName;

    // 类名
    private String className;

    // 方法名
    private String method;

    // cron表达式
    private String cron;

    // 执行者信息(ip:port)
    private String executor;

    // 状态 0：正常 1：禁用
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
