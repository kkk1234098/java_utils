package com.parkson.utils.core.encrypt;

import java.util.List;

public class Entity {

    private String name;
    private List<Child> childList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }
}
