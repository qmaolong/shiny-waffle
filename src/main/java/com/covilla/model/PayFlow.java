package com.covilla.model;

import com.covilla.model.mongo.BaseModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "pay_flow")
public class PayFlow extends BaseModel {
    @Id
    private Long id;

    private String orderNo;

    private String seriesNo;

    private BigDecimal amount;

    private String body;

    private String shopId;

    private String payType;

    private String appId;

    private String mchId;

    private String aliToken;

    private String authCode;

    private Integer payFor;

    private String payCard;

    private String chargeCard;

    private String state;

    private Date createTime;

    public PayFlow() {
    }

    public PayFlow(String orderNo, String seriesNo, BigDecimal amount, String body, String shopId, String payType, String appId, String mchId, String aliToken, String authCode, Integer payFor, String state) {
        this.orderNo = orderNo;
        this.seriesNo = seriesNo;
        this.amount = amount;
        this.body = body;
        this.shopId = shopId;
        this.payType = payType;
        this.appId = appId;
        this.mchId = mchId;
        this.aliToken = aliToken;
        this.authCode = authCode;
        this.payFor = payFor;
        this.state = state;
        this.createTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Integer getPayFor() {
        return payFor;
    }

    public void setPayFor(Integer payFor) {
        this.payFor = payFor;
    }

    public String getPayCard() {
        return payCard;
    }

    public void setPayCard(String payCard) {
        this.payCard = payCard;
    }

    public String getChargeCard() {
        return chargeCard;
    }

    public void setChargeCard(String chargeCard) {
        this.chargeCard = chargeCard;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAliToken() {
        return aliToken;
    }

    public void setAliToken(String aliToken) {
        this.aliToken = aliToken;
    }
}