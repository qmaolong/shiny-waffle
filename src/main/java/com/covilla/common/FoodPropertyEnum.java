package com.covilla.common;

/**
 * 菜品属性
 * Created by qmaolong on 2016/12/31.
 */
public enum  FoodPropertyEnum {
    temporaryFood("autoWeigh", "自动取重", false),
    timePrice("timePrice", "时价品项", false),
    volatilePrice("volatilePrice", "允许变价", true),
    temp("temp", "临时品项", false),
    canDiscount("canDiscount", "允许折扣", true),
    lowConsume("lowConsume", "计入低消", true);

    private String code;
    private String desc;
    private boolean defaultValue;

    FoodPropertyEnum(String code, String desc, boolean defaultValue) {
        this.code = code;
        this.desc = desc;
        this.defaultValue = defaultValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
}
