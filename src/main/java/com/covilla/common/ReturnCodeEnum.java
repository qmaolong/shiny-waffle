package com.covilla.common;

/**
 * Created by qmaolong on 2016/9/22.
 */
public enum ReturnCodeEnum {
    ;

    private String code;
    private String msg;

    ReturnCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
