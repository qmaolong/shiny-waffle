package com.covilla.model.mongo.card;

import com.covilla.model.mongo.BaseModel;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

@Document
public class CardChargeRule extends BaseModel {
    @Field("id")
    private Integer id;

    private String name;

    private Integer cardId;

    private Date startTime;

    private Date endTime;

    private BigDecimal minLine;

    private Integer giftType;

    private BigDecimal giftAmount;

    private Integer giftPercent;

    private Date createTime;

    private Integer creator;

    private String giftName;

    private String dayOn;

    private String dayOff;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getMinLine() {
        return minLine;
    }

    public void setMinLine(BigDecimal minLine) {
        this.minLine = minLine;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getGiftType() {
        return giftType;
    }

    public void setGiftType(Integer giftType) {
        this.giftType = giftType;
    }

    public BigDecimal getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(BigDecimal giftAmount) {
        this.giftAmount = giftAmount;
    }

    public Integer getGiftPercent() {
        return giftPercent;
    }

    public void setGiftPercent(Integer giftPercent) {
        this.giftPercent = giftPercent;
    }
    @Transient
    public String getGiftName() {
        if(this.giftType == 1){
            return "赠送" + this.giftAmount + "元";
        }else if(this.giftType == 2){
            return "赠送" + this.giftPercent + "%";
        }
        return giftName;
    }

    public String getDayOn() {
        return dayOn;
    }

    public void setDayOn(String dayOn) {
        this.dayOn = dayOn;
    }

    public String getDayOff() {
        return dayOff;
    }

    public void setDayOff(String dayOff) {
        this.dayOff = dayOff;
    }
}