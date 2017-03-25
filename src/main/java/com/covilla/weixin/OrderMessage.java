package com.covilla.weixin;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单信息推送实体类
 * 为保证信息处理端解析，需要保持当前目录
 * Created by kyumoka on 2017/3/3.
 * order message
 */
public class OrderMessage {
    private int type; // 0 - 消费，1-充值，2-撤销消费
    private String orderId;
    private String shopId;
    private String cardId;
    private BigDecimal amount;
    private BigDecimal changeAmount;
    private int point;
    private int changePoint;
    private Date time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getChangePoint() {
        return changePoint;
    }

    public void setChangePoint(int changePoint) {
        this.changePoint = changePoint;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
