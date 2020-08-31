package com.parkson.utils.core.encrypt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.parkson.utils.core.util.JsonUtil;
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

    public static String recursiveJsonObject(JSONArray jsonArray) {
        List<String> objectList = new ArrayList<>();
        Iterator<Object> arrayIter = jsonArray.iterator();
        while (arrayIter.hasNext()) {
            Map<String, Object> map = (Map<String, Object>) arrayIter.next();
            Set entSet = map.entrySet();
            Iterator<Map.Entry<String, Object>> mapIter = entSet.iterator();
            List<String> propertyList = new ArrayList<>();
            while (mapIter.hasNext()) {
                Map.Entry<String, Object> e = mapIter.next();
                if (JSONArray.class.isInstance(e.getValue())) {
                    String ret = recursiveJsonObject((JSONArray) e.getValue());
                    propertyList.add(e.getKey() + "=" + ret);
                } else {
                    if (e.getValue() != null) {
                        propertyList.add(e.getKey() + "=" + e.getValue());
                    }
                }
            }
            String objectStr = String.join("&", propertyList);
            objectList.add("{" + objectStr + "}");
        }
        String ret = String.join(",", objectList);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ret);
        sb.append("]");
        return sb.toString();
    }

    public static String generate(Map<String, Object> params, String appSecret) {
        List<String> list = new ArrayList<>();

        Set entrySet = params.entrySet();
        Iterator<Map.Entry<String, Object>> it = entrySet.iterator();
        //最外层提取
        while (it.hasNext()) {
            Map.Entry<String, Object> e = it.next();
            if (JSONArray.class.isInstance(e.getValue())) {
                String value = recursiveJsonObject((JSONArray) e.getValue());
                list.add(e.getKey() + "=" + value + "&");
            } else {
                list.add(e.getKey() + "=" + e.getValue() + "&");
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
        System.out.println("参数明文: " + result);

        return MD5Util.encrypt(result);
    }

    public static boolean check(Map<String, Object> params, String appSecret, String sign) {
        if (sign == null || sign.length() != 32) {
            return false;
        }

        String result = SignatureUtil.generate(params, appSecret);
        System.out.println("正确的签名: " + result);
        return StringUtils.equals(result, sign);
    }

    public static void main(String[] args) {
//        Child child1 = new Child();
//        child1.setName("chen");
//        child1.setAge(10);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", "");
        jsonObject1.put("age", 323);

        Child child2 = new Child();
        child2.setName(null);
        child2.setAge(33);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name", "chen2");
        jsonObject2.put("age", 33);

        Child child3 = new Child();
        child3.setName("");
        child3.setAge(33);
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("name", "chen3");
        jsonObject3.put("age", 33);

        JSONArray jsonArray33 = new JSONArray();
        JSONObject subObj = new JSONObject();
        subObj.put("name", "child");
        jsonArray33.add(subObj);
        jsonObject3.put("list", jsonArray33);


//        List<Child> list = new ArrayList<Child>();
//        list.add();
//        list.add(child2);
//        list.add(child3);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject3);

        Entity entity = new Entity();
        entity.setName("test");
        entity.setChildList(jsonArray);
        JSONObject parent = new JSONObject();
        parent.put("name", "tedst");
        parent.put("childList", jsonArray);

        Map<String, String> typeMap = new HashMap<>();
//        typeMap.put("childList", "List");

//        JsonUtil.toStringArrayMap()
        Map<String, Object> map = JsonUtil.toObjectMap(JSONObject.toJSONString(entity), null);
//        Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(entity), Map.class);
//        printJsonMap(map);

//        String sss = JsonUtil.toJSONString(entity);
//        System.out.println(sss);
//
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "ccccc");
//        map.put("list", list);
        generate(parent, "asdsadsadasd");

    }
}
