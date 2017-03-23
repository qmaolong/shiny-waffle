package com.covilla.vo.api;

import com.covilla.vo.BaseApiResultMsg;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/11/18.
 */
public class ReturnCardApiMsg extends BaseApiResultMsg {
    private String seriesNumber;
    private BigDecimal lastBalance;
    private BigDecimal withDrawAmount;

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public BigDecimal getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(BigDecimal lastBalance) {
        this.lastBalance = lastBalance;
    }

    public BigDecimal getWithDrawAmount() {
        return withDrawAmount;
    }

    public void setWithDrawAmount(BigDecimal withDrawAmount) {
        this.withDrawAmount = withDrawAmount;
    }
}
