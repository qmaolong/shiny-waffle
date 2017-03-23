package com.covilla.vo;

/**
 * Created by qmaolong on 2016/9/23.
 */
public class BaseApiResultMsg {
    private String errcode = "0";
    private String msg = "ok";

    public static BaseApiResultMsg buildErrorMsg(String errcode, String msg){
        BaseApiResultMsg baseApiResultMsg = new BaseApiResultMsg();
        baseApiResultMsg.setErrcode(errcode);
        baseApiResultMsg.setMsg(msg);

        return baseApiResultMsg;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
