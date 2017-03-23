package com.covilla.model.mongo.card;

import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 卡券发放规则
 * Created by qmaolong on 2016/12/21.
 */
public class CardPublish {
    private Integer id;
    private String name;
    private Date startTime;
    private Date endTime;
    @Field("fitDay")
    private String fitDays;
    private String dayOn;
    private String dayOff;
    private BigDecimal minLine;
    private Boolean repeat;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId ticket;
    private Integer count;
//    private Integer dataStatus = 0;
    /*@Transient
    private String cardTypeIdStr;*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFitDays() {
        return fitDays;
    }

    public void setFitDays(String fitDays) {
        this.fitDays = fitDays;
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

    public BigDecimal getMinLine() {
        return minLine;
    }

    public void setMinLine(BigDecimal minLine) {
        this.minLine = minLine;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public ObjectId getTicket() {
        return ticket;
    }

    public void setTicket(ObjectId ticket) {
        this.ticket = ticket;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    /*public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /*public String getCardTypeIdStr() {
        if(null != ticket){
            return ticket.toString();
        }
        return cardTypeIdStr;
    }

    public void setCardTypeIdStr(String cardTypeIdStr) {
        this.cardTypeIdStr = cardTypeIdStr;
    }*/
}
