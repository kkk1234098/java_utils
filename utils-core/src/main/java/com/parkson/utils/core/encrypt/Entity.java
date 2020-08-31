package com.parkson.utils.core.encrypt;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

public class Entity {

    private String name;
    private JSONArray childList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONArray getChildList() {
        return childList;
    }

    public void setChildList(JSONArray childList) {
        this.childList = childList;
    }
}
