package com.covilla.common;

/**
 * Created by qmaolong on 2016/9/26.
 */
public enum CardChargeTypeEnum {
    backend(1, "后台充值"),
    client(2, "客户端"),
    weixin(3, "公众平台");

    private Integer code;
    private String name;

    CardChargeTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
