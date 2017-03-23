package com.covilla.model.mongo.card;

import com.covilla.model.mongo.BaseModel;
import com.covilla.util.ValidatorUtil;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document
public class CardDiscountRule extends BaseModel {
    @Transient
    public String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
    @Field("id")
    private Integer id;

    private String name;

    private Integer cardId;

    private Date startTime;

    private Date endTime;

    private Integer discountType;

    private Double minLine;

    private Integer percent;

    private Double reduction;

    private String fitDays;

    private Date createTime;

    private Integer creator;
    @Field("fitFoods")
    private List<CardRuleFitFood> fitFoods;
    @Transient
    private String discountName;
    @Transient
    private String fitDaysCH;

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

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Double getMinLine() {
        return minLine;
    }

    public void setMinLine(Double minLine) {
        this.minLine = minLine;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Double getReduction() {
        return reduction;
    }

    public void setReduction(Double reduction) {
        this.reduction = reduction;
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

    public List<CardRuleFitFood> getFitFoods() {
        return fitFoods;
    }

    public void setFitFoods(List<CardRuleFitFood> fitFoods) {
        this.fitFoods = fitFoods;
    }

    public String getFitDays() {
        return fitDays;
    }

    public void setFitDays(String fitDays) {
        this.fitDays = fitDays;
    }

    public String getDiscountName() {
        if(1 == this.discountType && ValidatorUtil.isNotNull(this.percent)){
            return this.percent/10 + "折";
        }else if(2 == this.discountType){
            return "减免" + this.reduction + "元";
        }else if(3 == this.discountType){
            return "特价";
        }
        return "";
    }

    public String[] getWeeks() {
        return weeks;
    }

    //获取适用星期
    public String getFitDaysCH() {
        if(null == this.fitDays || "" == this.fitDays){
            return "-";
        }
        StringBuffer stringBuffer = new StringBuffer("星期");
        for(int i=0; i<this.fitDays.length(); i++){
            if("1".equals(this.fitDays.substring(i, i+1))){
                stringBuffer.append(weeks[i]).append("、");
            }
        }
        stringBuffer.delete(stringBuffer.length()-1, stringBuffer.length());
        return stringBuffer.toString();
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