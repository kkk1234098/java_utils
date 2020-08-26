package com.parkson.utils.core.util;

import com.alibaba.fastjson.JSONObject;
import com.parkson.utils.core.util.JsonUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @Author hc
 * @Date 2020/07/10
 * 字符串转换工具类
 */
public class JavaUsedUtil {
    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->HELLO_WORLD
     * @param name 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return result.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
     * 例如：HELLO_WORLD->HelloWorld
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 将对象的key全部转换为驼峰
     * @param object
     * @return
     */
    public static Object getObjectPutChangeKeys(Object object) {
        JSONObject jsonObject = JsonUtil.map2Object(object, JSONObject.class);
        Iterator<String> keys = jsonObject.keySet().iterator();
        JSONObject json = new JSONObject();
        while (keys.hasNext()) {
            String old_key = String.valueOf(keys.next());
            String key = JavaUsedUtil.camelName(old_key);
            json.put(key, jsonObject.getString(old_key));
        }

        return json;
    }

    /**
     * 将对象的key全部转换为驼峰
     * @param object
     * @return
     */
    public static JSONObject getJsonObjectPutChangeKeys(Object object) {
        JSONObject jsonObject = JsonUtil.map2Object(object, JSONObject.class);
        Iterator<String> keys = jsonObject.keySet().iterator();
        JSONObject json = new JSONObject();
        while (keys.hasNext()) {
            String old_key = String.valueOf(keys.next());
            String key = JavaUsedUtil.camelName(old_key);
            json.put(key, jsonObject.getString(old_key));
        }

        return json;
    }

    /**
     * @param path 图片路径
     * @return
     * @Descriptionmap 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @author hc
     * @Date 2020/7/20
     */
    public static String imageToBase64(String path) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        //读取文件后缀名
        int lastIndexOf = path.lastIndexOf(".");
        String suffix = path.substring(lastIndexOf + 1);
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return "data:image/" + suffix + ";base64," + encoder.encode(data);// 返回Base64编码过的字节数组字符串
//        return encoder.encode(data);
    }

    /**
     * @param base64 图片Base64数据
     * @param path   图片路径
     * @return
     * @Descriptionmap 对字节数组字符串进行Base64解码并生成图片
     * @author temdy
     * @Date 2015-01-26
     */
    public static boolean base64ToImage(String base64, String path) {// 对字节数组字符串进行Base64解码并生成图片
        if (base64 == null) { // 图像数据为空
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将带有base64头内的后缀名与base64码,size分别解析出来
     * @param base64
     * @return
     */
    public static List<String> base64ToBase64List(String base64) {
        int indexOfStart = base64.indexOf("/");
        int indexOfEnd = base64.indexOf(";");
        String suffix = base64.substring(indexOfStart + 1, indexOfEnd);
        List<String> stringList = new ArrayList<>();
        stringList.add(suffix);
        indexOfStart = base64.indexOf(",");
        String base64string = base64.substring(indexOfStart + 1);
        stringList.add(base64string);
        int size = stringList.get(1).length();
        String tail = base64string.substring(size - 10);
        int equalIndex = tail.indexOf("=");
        if (equalIndex > 0) {
            size = size - (10 - equalIndex);
        }
        double sizeDouble = (size - ((double) size / 8) * 2);
        stringList.add(String.valueOf(sizeDouble));
        return stringList;
    }

}
