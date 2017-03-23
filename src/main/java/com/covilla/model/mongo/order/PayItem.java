package com.covilla.model.mongo.order;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/10/13.
 */
public class PayItem {
    private Integer type;
    private BigDecimal amount;
    private String payId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }
}
