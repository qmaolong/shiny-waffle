package com.covilla.vo.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.covilla.model.mongo.card.CardType;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.covilla.vo.BaseApiResultMsg;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by qmaolong on 2016/9/23.
 */
@Document(collection = "card")
public class CardApiMsg extends BaseApiResultMsg {
    private String no;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId shopId;
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Field("cardType")
    private ObjectId cardTypeId;
    private String name;
    private Integer medium;
    private String memberName;
    private String tel;
    private String birthDay;
    private String openId;
    private Date activateTime;
    private String mediumKey;
    private Double balance = 0.0;
    private Integer source;
    private Integer point = 0;
    @Transient
    private CardType cardTypeObject;
    private Integer cardState;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @JSONField(serialize=false,deserialize=false)
    public ObjectId getShopId() {
        return shopId;
    }

    public void setShopId(ObjectId shopId) {
        this.shopId = shopId;
    }

    public ObjectId getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(ObjectId cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMedium() {
        return medium;
    }

    public void setMedium(Integer medium) {
        this.medium = medium;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Date activateTime) {
        this.activateTime = activateTime;
    }

    public String getMediumKey() {
        return mediumKey;
    }

    public void setMediumKey(String mediumKey) {
        this.mediumKey = mediumKey;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getCardState() {
        return cardState;
    }

    public void setCardState(Integer cardState) {
        this.cardState = cardState;
    }

    public CardType getCardTypeObject() {
        return cardTypeObject;
    }

    public void setCardTypeObject(CardType cardTypeObject) {
        this.cardTypeObject = cardTypeObject;
    }
}
