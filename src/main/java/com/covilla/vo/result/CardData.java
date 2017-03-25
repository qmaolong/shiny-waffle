package com.covilla.vo.result;

import java.math.BigDecimal;

/**
 * Created by qmaolong on 2016/12/15.
 */
public class CardData {
    private String shopName;//消费的门店
    private Boolean superFlag;
    private String dateStr;
    private String cardType;//卡类型
    private Boolean isPublic;
    private Boolean isCoupon;
    private BigDecimal chargeAmount = BigDecimal.ZERO;
    private BigDecimal payAmount = BigDecimal.ZERO;
    private BigDecimal returnAmount = BigDecimal.ZERO;
    private BigDecimal discountAmount = BigDecimal.ZERO;//优惠金额

    /*public CardData(String shopName, Boolean superFlag, String dateStr, String cardType) {
        this.shopName = shopName;
        this.superFlag = superFlag;
        this.dateStr = dateStr;
        this.cardType = cardType;
    }*/

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(Boolean coupon) {
        isCoupon = coupon;
    }
}
