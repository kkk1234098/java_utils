package com.parkson.utils.wechat.entity;

/**
 * @ClassName WechatAccessTokenResponse
 * @Description 小程序后端调用微信接口使用的AccessToken
 * @Author chenyijun
 * @Date 2020/9/8 10:21 上午
 * @Version 1.0
 */
public class WechatAccessTokenResponse {

    private  String accessToken;

    private int expiresIn;

    private Integer errCode;

    private String errMsg;

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

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
