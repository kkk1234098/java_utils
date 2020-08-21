package com.parkson.utils.core.encrypt;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @ClassName SignatureUtil
 * @Description TO DO
 * @Author cheneason
 * @Date 2020/8/11 14:05
 * @Version 1.0
 */
public class SignatureUtil {

    public static String generate(Map<String, Object> params, String appSecret) {
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                if (String.class.isInstance(entry.getValue())) {
                    if (StringUtils.isNotBlank((String) entry.getValue())) {
                        list.add(entry.getKey() + "=" + entry.getValue() + "&");
                    }
                } else {
                    list.add(entry.getKey() + "=" + entry.getValue() + "&");
                }
            }
        }

        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "secret=" + appSecret;
        System.out.println(result);

        return MD5Util.encrypt(result);
    }

    public static boolean check(Map<String, Object> params, String appSecret, String sign) {
        if (sign == null || sign.length() != 32) {
            return false;
        }

        String result = SignatureUtil.generate(params, appSecret);
        return StringUtils.equals(result, sign);
    }

    public static void main(String[] args) {
        Child child1 = new Child();
        child1.setName("chen");
        child1.setAge(10);

        Child child2 = new Child();
        child2.setName("chen333");
        child2.setAge(33);

        Map<String, String> map1 = new HashMap<>();
        map1.put("name", "ch en");
        map1.put("age", "333");

        Map<String, String> map2 = new HashMap<>();
        map2.put("name", "chen2");
        map2.put("age", "3333");

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(map1);
        list.add(map2);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "ccccc");
        map.put("list", list);
        generate(map, "asdsadsadasd");


        System.out.println("asds");
    }
}
