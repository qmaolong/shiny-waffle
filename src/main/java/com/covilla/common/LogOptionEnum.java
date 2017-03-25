package com.covilla.common;

/**
 * 操作日志类型
 * Created by qmaolong on 2017/2/11.
 */
public enum  LogOptionEnum {
    CRUD("crud"),
    BUSINESS("business"),
    GENERATE_CARD("generate");

    private String name;

    LogOptionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
