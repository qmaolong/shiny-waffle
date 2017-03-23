package com.covilla.vo.result;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/12/14.
 */
public class PayTypeData {
    private String payType;
    private String payTypeName;
    private Integer payTimes = 0;
    private BigDecimal payAmount = BigDecimal.ZERO;
    private String dateStr;

    public PayTypeData(String payType, String payTypeName, Integer payTimes, BigDecimal payAmount, String dateStr) {
        this.payType = payType;
        this.payTypeName = payTypeName;
        this.payTimes = payTimes;
        this.payAmount = payAmount;
        this.dateStr = dateStr;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public Integer getPayTimes() {
        return payTimes;
    }

    public void setPayTimes(Integer payTimes) {
        this.payTimes = payTimes;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}
