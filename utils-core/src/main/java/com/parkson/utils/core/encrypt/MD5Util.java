package com.parkson.utils.core.encrypt;

import org.springframework.util.DigestUtils;

/**
 * @ClassName MD5Encrypt
 * @Description TO DO
 * @Author cheneason
 * @Date 2020/8/11 13:28
 * @Version 1.0
 */
public class MD5Util {

    /**
     * 字符串做MD5加密
     * @param text
     * @return
     */
    public static String encrypt(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }
}
