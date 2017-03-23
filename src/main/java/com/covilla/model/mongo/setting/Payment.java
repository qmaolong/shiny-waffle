package com.covilla.model.mongo.setting;

import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * Created by qmaolong on 2016/11/4.
 */
public class Payment {
    private String name;
    private String describe;
    private Boolean activated = false;
    private Boolean givePoint = false;
    private String abc;
    private String unit;
    private Boolean giveTicket = false;
    //weixin pay
    private String wxAppId;
    private String wxMchId;
    //ali pay
    @Transient
    private String authCode;
    private String alAppId;//appid
    private String alAuthToken;//授权token
    private String alUserId;
    private Date alTokenExpireDate;//授权token过期时间
    private String alRefreshToken;//刷新token
    private Date alRefreshTokenExpireDate;//刷新token过期时间


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getWxMchId() {
        return wxMchId;
    }

    public void setWxMchId(String wxMchId) {
        this.wxMchId = wxMchId;
    }

    public String getAlAppId() {
        return alAppId;
    }

    public void setAlAppId(String alAppId) {
        this.alAppId = alAppId;
    }

    public Boolean getGivePoint() {
        return givePoint;
    }

    public void setGivePoint(Boolean givePoint) {
        this.givePoint = givePoint;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getGiveTicket() {
        return giveTicket;
    }

    public void setGiveTicket(Boolean giveTicket) {
        this.giveTicket = giveTicket;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAlAuthToken() {
        return alAuthToken;
    }

    public void setAlAuthToken(String alAuthToken) {
        this.alAuthToken = alAuthToken;
    }

    public String getAlUserId() {
        return alUserId;
    }

    public void setAlUserId(String alUserId) {
        this.alUserId = alUserId;
    }

    public Date getAlTokenExpireDate() {
        return alTokenExpireDate;
    }

    public void setAlTokenExpireDate(Date alTokenExpireDate) {
        this.alTokenExpireDate = alTokenExpireDate;
    }

    public String getAlRefreshToken() {
        return alRefreshToken;
    }

    public void setAlRefreshToken(String alRefreshToken) {
        this.alRefreshToken = alRefreshToken;
    }

    public Date getAlRefreshTokenExpireDate() {
        return alRefreshTokenExpireDate;
    }

    public void setAlRefreshTokenExpireDate(Date alRefreshTokenExpireDate) {
        this.alRefreshTokenExpireDate = alRefreshTokenExpireDate;
    }
}
