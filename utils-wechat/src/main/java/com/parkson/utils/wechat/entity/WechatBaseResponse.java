package com.parkson.utils.wechat.entity;

/**
 * @ClassName WechatBaseResponse
 * @Description TO DO
 * @Author chenyijun
 * @Date 2020/10/28 10:59 上午
 * @Version 1.0
 */
public class WechatBaseResponse {

    private Integer errCode;

    private String errMsg;

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
