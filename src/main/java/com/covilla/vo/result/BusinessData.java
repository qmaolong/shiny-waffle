package com.covilla.vo.result;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/11/6.
 */
public class BusinessData {
    private String shopName = "";
    private Boolean superFlag = false;
    private String dateStr = "";

    //订单相关
    private BigDecimal totalAmount = BigDecimal.ZERO;//计算出的商品总价格，包含了所有原始应该销售的价格（赠送价格和品项折扣的也计入了）
    private Integer orderCount = 0;//订单数
    private Integer people = 0;//来客数
    private BigDecimal subAmount = BigDecimal.ZERO;//小计
    private BigDecimal amount = BigDecimal.ZERO;//用户将要结账的金额

    //订单类型相关
    private BigDecimal deskOrderAmount = BigDecimal.ZERO;//堂食
    private BigDecimal fastOrderAmount = BigDecimal.ZERO;//快餐
    private BigDecimal takeOutAmount = BigDecimal.ZERO;//外卖

    //收入相关
    private BigDecimal businessIncome = BigDecimal.ZERO;//营业收入 = 堂食 + 快餐 + 外卖
    private BigDecimal extraIncome = BigDecimal.ZERO;//额外收入 = 意外出现的收入或亏损
    private BigDecimal totalIncome = BigDecimal.ZERO;//总收入 = 营业收入 + 额外收入 + 充值金额 - 卡消费 - 退卡金额

    //支付方式相关
    private BigDecimal cashAmount = BigDecimal.ZERO;//现金支付
    private BigDecimal bankAmount = BigDecimal.ZERO;//银行卡支付
    private BigDecimal cardAmount = BigDecimal.ZERO;//储值卡支付
    private BigDecimal couponAmount = BigDecimal.ZERO;//券支付
    private BigDecimal otherAmount = BigDecimal.ZERO;//其他支付方式

    //卡相关
    private BigDecimal chargeAmount = BigDecimal.ZERO;//充值金额
    private BigDecimal returnCardAmount = BigDecimal.ZERO;//退卡金额
//    private BigDecimal cardBalance = BigDecimal.ZERO;//储值卡总余额

    //折扣相关
    private BigDecimal discountAmount = BigDecimal.ZERO;//整单折扣
    private BigDecimal specialDiscountAmount = BigDecimal.ZERO;//特殊折扣
    private BigDecimal itemDiscountAmount = BigDecimal.ZERO;
    private BigDecimal cardDiscountAmount = BigDecimal.ZERO;
    private BigDecimal couponDiscountAmount = BigDecimal.ZERO;

    public Boolean getSuperFlag() {
        return superFlag;
    }

    public void setSuperFlag(Boolean superFlag) {
        this.superFlag = superFlag;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public BigDecimal getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(BigDecimal bankAmount) {
        this.bankAmount = bankAmount;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(BigDecimal subAmount) {
        this.subAmount = subAmount;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(BigDecimal otherAmount) {
        this.otherAmount = otherAmount;
    }

    public BigDecimal getReturnCardAmount() {
        return returnCardAmount;
    }

    public void setReturnCardAmount(BigDecimal returnCardAmount) {
        this.returnCardAmount = returnCardAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getSpecialDiscountAmount() {
        return specialDiscountAmount;
    }

    public void setSpecialDiscountAmount(BigDecimal specialDiscountAmount) {
        this.specialDiscountAmount = specialDiscountAmount;
    }

    public BigDecimal getItemDiscountAmount() {
        return itemDiscountAmount;
    }

    public void setItemDiscountAmount(BigDecimal itemDiscountAmount) {
        this.itemDiscountAmount = itemDiscountAmount;
    }

    public BigDecimal getCardDiscountAmount() {
        return cardDiscountAmount;
    }

    public void setCardDiscountAmount(BigDecimal cardDiscountAmount) {
        this.cardDiscountAmount = cardDiscountAmount;
    }

    public BigDecimal getCouponDiscountAmount() {
        return couponDiscountAmount;
    }

    public void setCouponDiscountAmount(BigDecimal couponDiscountAmount) {
        this.couponDiscountAmount = couponDiscountAmount;
    }

    public BigDecimal getBusinessIncome() {
        return businessIncome;
    }

    public void setBusinessIncome(BigDecimal businessIncome) {
        this.businessIncome = businessIncome;
    }

    public BigDecimal getExtraIncome() {
        return extraIncome;
    }

    public void setExtraIncome(BigDecimal extraIncome) {
        this.extraIncome = extraIncome;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    /*public BigDecimal getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(BigDecimal cardBalance) {
        this.cardBalance = cardBalance;
    }*/

    public BigDecimal getDeskOrderAmount() {
        return deskOrderAmount;
    }

    public void setDeskOrderAmount(BigDecimal deskOrderAmount) {
        this.deskOrderAmount = deskOrderAmount;
    }

    public BigDecimal getFastOrderAmount() {
        return fastOrderAmount;
    }

    public void setFastOrderAmount(BigDecimal fastOrderAmount) {
        this.fastOrderAmount = fastOrderAmount;
    }

    public BigDecimal getTakeOutAmount() {
        return takeOutAmount;
    }

    public void setTakeOutAmount(BigDecimal takeOutAmount) {
        this.takeOutAmount = takeOutAmount;
    }
}
