package com.parkson.utils.wechat.entity;

/**
 * @ClassName PushTemplateElement
 * @Description TO DO
 * @Author chenyijun
 * @Date 2020/11/6 2:58 下午
 * @Version 1.0
 */
public class PushTemplateElement {

    public String value;

    public PushTemplateElement(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
