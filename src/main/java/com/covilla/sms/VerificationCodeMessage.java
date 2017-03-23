package com.covilla.sms;

import java.io.Serializable;
import java.util.Date;

/**
 *  短信推送实体类
 * 为保证信息处理端解析，需要保持当前目录
 * Created by qmaolong on 2017/3/10.
 */
public class VerificationCodeMessage implements Serializable {
    private String tel;
    private String code;
    private String action;
    private String shopId;
    private Date time;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getShopId() {
        return shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
}