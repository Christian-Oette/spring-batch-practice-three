package com.christianoette.dontchangeit.dto;

public class JsonWrapper {
    private Object value;

    public JsonWrapper(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
