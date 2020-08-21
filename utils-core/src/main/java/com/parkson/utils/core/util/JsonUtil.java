package com.parkson.utils.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName JsonUtil
 * @Description TO DO
 * @Author cheneason
 * @Date 2020/7/19 00:06
 * @Version 1.0
 */
public class JsonUtil {

    private static final String SerialWriterString = "序列化JSON串发生异常";
    private static final HashMap<String, String> DefaultTypeMap = new HashMap<String, String>();

    public static <T> T toJavaObject(String s, Class<T> clazz) {
        return JSONObject.parseObject(s, clazz);
    }

    public static <T> T toJavaObject(String s, Type clazz) {
        return JSONObject.parseObject(s, clazz);
    }

    public static <T> T toJavaObject2(String s, TypeReference<T> t) {
        return JSONObject.parseObject(s, t);
    }

    public static <T> List<T> toJavaObjectArray(String s, Class<T> clazz) {
        return JSONObject.parseArray(s, clazz);
    }

    public static String toJSONString(Object o) {
        try {
            if (o instanceof List) {
                return JSONArray.toJSONString(o);
            } else {
                return JSONObject.toJSONString(o);
            }
        } catch (Exception e) {
            return SerialWriterString;
        }
    }

    /**
     * @param o
     * @param prettyFormat 格式化输出JSONString
     * @return
     */
    public static String toJSONString(Object o, boolean prettyFormat) {
        try {
            return JSONObject.toJSONString(o, prettyFormat);
        } catch (Exception e) {
            // TODO: handle exception
            return SerialWriterString;
        }
    }

    /**
     * @param o
     * @return
     */
    public static String toWriteNullJSONString(Object o) {
        SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
                SerializerFeature.WriteNullListAsEmpty,
                // list字段如果为null，输出为[]，而不是null
                SerializerFeature.WriteNullNumberAsZero,
                // 数值字段如果为null，输出为0，而不是null
                SerializerFeature.WriteNullBooleanAsFalse,
                // Boolean字段如果为null，输出为false，而不是null
                SerializerFeature.WriteNullStringAsEmpty
                // 字符类型字段如果为null，输出为""，而不是null
        };

        try {
            return JSONObject.toJSONString(o, features);
        } catch (Exception e) {
            // TODO: handle exception
            return SerialWriterString;
        }
    }

    public static <K, V> String map2JSONString(Map<K, V> map) {
        return toJSONString(map);
    }

    public static <T> String list2JSONString(List<T> list) {
        try {
            // return JSONArray.toJSONString(list, mapping);
            return JSONArray.toJSONString(list);
        } catch (Exception e) {
            // TODO: handle exception
            return SerialWriterString;
        }
    }

    /**
     * 将一个json字符串转换成支持列表嵌套结构的MAP（仅限一层嵌套） 没有在typeMap中定义类型的，默认一律转换类型为String
     *
     * @param s       json字符串
     * @param typeMap 类型map表 key: 键名，例如：sr_country value:
     *                类型名：支持：BigDecimal,Date,Integer,String
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toObjectMap(String s, Map<String, String> typeMap) {
        if (typeMap == null) {
            typeMap = DefaultTypeMap;
        }

        Map<String, Object> map = (Map<String, Object>) toJavaObject(s, Map.class);
        Object obj;
        for (String key : map.keySet()) {
            obj = map.get(key);
            if (obj instanceof JSONArray) // 内嵌的List<Map<String, Object>>类型
            {
                JSONArray arr = (JSONArray) obj;
                List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
                for (Object o : arr) {
                    JSONObject jobj = (JSONObject) o;
                    // 递归解析子map
                    Map<String, Object> subMap = toObjectMap(jobj.toJSONString(), typeMap);
                    subList.add(subMap);
                }
                map.put(key, subList);
            } else {
                if (!typeMap.containsKey(key)) // 默认采用String类型
                {
                    map.put(key, String.valueOf(obj));
                } else {
                    String type = typeMap.get(key);
                    if (type.equalsIgnoreCase("BigDecimal")) {
                        map.put(key, new BigDecimal(String.valueOf(obj)));
                    } else if (type.equalsIgnoreCase("Date")) {
                        map.put(key, new Date(Long.valueOf(String.valueOf(obj))));
                    } else if (type.equalsIgnoreCase("Integer")) {
                        map.put(key, Integer.valueOf(String.valueOf(obj)));
                    } else {
                        map.put(key, String.valueOf(obj));
                    }
                }
            }
        }

        return map;
    }

    /**
     * 将一个json字符串转换成Map<String, String[]>类型
     *
     * @param s
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String[]> toStringArrayMap(String s) {
        Map<String, Object> map = (Map<String, Object>) toJavaObject(s, Map.class);
        Map<String, String[]> rtnMap = new HashMap<String, String[]>();
        Object obj;
        for (String key : map.keySet()) {
            obj = map.get(key);
            if (obj instanceof JSONArray) {
                Object[] objArr = ((JSONArray) obj).toArray();
                if (objArr != null && objArr.length > 0) {
                    int len = objArr.length;
                    String[] strArr = new String[len];
                    for (int i = 0; i < len; i++) {
                        strArr[i] = String.valueOf(objArr[i]);
                    }
                    rtnMap.put(key, strArr);
                }
            }
        }
        return rtnMap;
    }

    public static <T> T map2Object(Object map, Type clazz) {
        String jsonString = JsonUtil.toJSONString(map);
        return JSONObject.parseObject(jsonString, clazz);
    }
}
