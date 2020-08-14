package com.parkson.utils.core.encrypt;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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

        return MD5Util.encrypt(result);
    }

    public static boolean check(Map<String, Object> params, String appSecret, String sign) {
        if (sign == null || sign.length() != 32) {
            return false;
        }

        String result = SignatureUtil.generate(params, appSecret);
        return StringUtils.equals(result, sign);
    }
}
