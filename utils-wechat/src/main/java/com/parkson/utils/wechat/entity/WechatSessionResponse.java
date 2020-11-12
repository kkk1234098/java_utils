package com.parkson.utils.wechat.entity;

/**
 * @ClassName WechatSessionResponse
 * @Description TO DO
 * @Author chenyijun
 * @Date 2020/9/3 3:36 下午
 * @Version 1.0
 */
public class WechatSessionResponse extends WechatBaseResponse {

    private  String accessToken;

    private int expiresIn;

    private String refreshToken;

    private String openId;

    private String unionId;

    private String scope;

    private String sessionKey;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
