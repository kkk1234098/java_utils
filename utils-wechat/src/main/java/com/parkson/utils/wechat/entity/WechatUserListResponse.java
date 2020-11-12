package com.parkson.utils.wechat.entity;

import java.util.List;

/**
 * @ClassName WechatUserListResponse
 * @Description TO DO
 * @Author chenyijun
 * @Date 2020/10/27 5:12 下午
 * @Version 1.0
 */
public class WechatUserListResponse extends WechatBaseResponse {

    private Long total;

    private Long count;

    private List<String> openIdList;

    private String nextOpenId;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<String> getOpenIdList() {
        return openIdList;
    }

    public void setOpenIdList(List<String> openIdList) {
        this.openIdList = openIdList;
    }

    public String getNextOpenId() {
        return nextOpenId;
    }

    public void setNextOpenId(String nextOpenId) {
        this.nextOpenId = nextOpenId;
    }
}
