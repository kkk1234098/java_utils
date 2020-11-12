package com.parkson.utils.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.parkson.utils.wechat.entity.PushTemplateCollection;
import com.parkson.utils.wechat.entity.PushTemplateElement;
import org.springframework.util.ReflectionUtils;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PushTemplateUtil
 * @Description TO DO
 * @Author chenyijun
 * @Date 2020/11/6 2:57 下午
 * @Version 1.0
 */
public class PushTemplateUtil {

    public static JSONObject toJsonObject(PushTemplateCollection collection) {
        JSONObject jsonObject = new JSONObject();
        List<Field> fieldList = new ArrayList<>();
        ReflectionUtils.doWithFields(PushTemplateCollection.class, field -> {
            fieldList.add(field);
        });

        fieldList.forEach(field -> {
            Method method = ReflectionUtils.findMethod(collection.getClass(), "get" + upperFirstCase(field.getName()));
            if (method != null) {

                PushTemplateElement element = (PushTemplateElement) ReflectionUtils.invokeMethod(method, collection);
                if (element != null && element.getValue() != null) {
                    JSONObject elementJson = new JSONObject();
                    elementJson.put("value", element.getValue());
                    jsonObject.put(field.getName(), elementJson);
                }
            }
        });

        return jsonObject;
    }

    /**
     * 转换首字母小写
     *
     * @param str
     * @return
     */
    public static String upperFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }

    public static void main(String[] args) {

        PushTemplateCollection pushTemplateCollection = new PushTemplateCollection();
        pushTemplateCollection.setFirst(new PushTemplateElement("adssda"));
        pushTemplateCollection.setRemark(new PushTemplateElement("remark"));
        PushTemplateUtil.toJsonObject(pushTemplateCollection);
    }
}
