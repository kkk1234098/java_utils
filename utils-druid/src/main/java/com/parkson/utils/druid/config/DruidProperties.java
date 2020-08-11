package com.parkson.utils.druid.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName DruidProperties
 * @Description TO DO
 * @Author cheneason
 * @Date 2020/7/30 10:54
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "druid.prop")
@Component
public class DruidProperties {

    private String loginUsername;
    private String loginPassword;
    private String allow;
    private String deny;

    public DruidProperties() {
        this.loginUsername = "admin";
        this.loginPassword = "123456";
        this.allow = "";
        this.deny = "";
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getAllow() {
        return allow;
    }

    public void setAllow(String allow) {
        this.allow = allow;
    }

    public String getDeny() {
        return deny;
    }

    public void setDeny(String deny) {
        this.deny = deny;
    }
}
