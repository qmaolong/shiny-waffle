package com.covilla.model.mongo.card;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document
public class CardRuleFitFood {
    @Transient
    private Integer id;

    private Integer ccrId;

    private Integer cdrId;
    @Field("id")
    private String foodId;

    private String foodName;

    private Date createTime;

    private Integer dataStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCcrId() {
        return ccrId;
    }

    public void setCcrId(Integer ccrId) {
        this.ccrId = ccrId;
    }

    public Integer getCdrId() {
        return cdrId;
    }

    public void setCdrId(Integer cdrId) {
        this.cdrId = cdrId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }
}