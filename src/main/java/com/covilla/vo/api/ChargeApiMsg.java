package com.covilla.vo.api;

import com.covilla.vo.BaseApiResultMsg;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/9/23.
 */
public class ChargeApiMsg extends BaseApiResultMsg {
    private String seriesNumber;
    private BigDecimal chargeAmount;
    private BigDecimal giftAmount;
    private BigDecimal lastBalance;
    private BigDecimal curBalance;

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public BigDecimal getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(BigDecimal lastBalance) {
        this.lastBalance = lastBalance;
    }

    public BigDecimal getCurBalance() {
        return curBalance;
    }

    public void setCurBalance(BigDecimal curBalance) {
        this.curBalance = curBalance;
    }

    public BigDecimal getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(BigDecimal giftAmount) {
        this.giftAmount = giftAmount;
    }
}
