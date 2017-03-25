package com.covilla.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "card_flow")
public class CardFlow {
    @Id
    private Long id;

    private String seriesNo;

    private String cardOwnerShop;

    private String cardUseShop;

    private Boolean isPublic;

    private Integer option;

    private BigDecimal amount;

    private BigDecimal giftAmount;

    private Integer chargeType;

    private String cardId;

    private Integer cardMedium;

    private String cardMediumKey;

    private String cardTagNo;

    private String orderNo;

    private BigDecimal orderAmount;

    private BigDecimal lastBalance;

    private BigDecimal curBalance;

    private Integer lastPoint;

    private Integer curPoint;

    private Integer isRollback;

    private String rollbackId;

    private String remark;

    private String creator;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getCardOwnerShop() {
        return cardOwnerShop;
    }

    public void setCardOwnerShop(String cardOwnerShop) {
        this.cardOwnerShop = cardOwnerShop;
    }

    public String getCardUseShop() {
        return cardUseShop;
    }

    public void setCardUseShop(String cardUseShop) {
        this.cardUseShop = cardUseShop;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getOption() {
        return option;
    }

    public void setOption(Integer option) {
        this.option = option;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(BigDecimal giftAmount) {
        this.giftAmount = giftAmount;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Integer getCardMedium() {
        return cardMedium;
    }

    public void setCardMedium(Integer cardMedium) {
        this.cardMedium = cardMedium;
    }

    public String getCardMediumKey() {
        return cardMediumKey;
    }

    public void setCardMediumKey(String cardMediumKey) {
        this.cardMediumKey = cardMediumKey;
    }

    public String getCardTagNo() {
        return cardTagNo;
    }

    public void setCardTagNo(String cardTagNo) {
        this.cardTagNo = cardTagNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
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

    public Integer getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(Integer lastPoint) {
        this.lastPoint = lastPoint;
    }

    public Integer getCurPoint() {
        return curPoint;
    }

    public void setCurPoint(Integer curPoint) {
        this.curPoint = curPoint;
    }

    public Integer getIsRollback() {
        return isRollback;
    }

    public void setIsRollback(Integer isRollback) {
        this.isRollback = isRollback;
    }

    public String getRollbackId() {
        return rollbackId;
    }

    public void setRollbackId(String rollbackId) {
        this.rollbackId = rollbackId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}