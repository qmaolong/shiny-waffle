package com.covilla.model.mongo.card;

import com.covilla.model.mongo.BaseModel;
import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.covilla.util.ValidatorUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by qmaolong on 2016/9/22.
 */
@Document(collection = "card")
public class Card extends BaseModel {
    public static Integer SOURCE_BACKEND = 0;
    public static Integer SOURCE_CASHIER = 1;
    public static Integer SOURCE_WEIXIN = 2;
    //    private String no;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId shopId;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Field("cardType")
    private ObjectId cardTypeId;
    private String name;
    private Integer medium;
    private String memberName;
    private String tel;
    private Date birthDay;
    private String sex;
    private String openId;
    private Integer cardState;
    private Date activateTime;
    private String mediumKey;
    private BigDecimal balance;
    private Integer source;
    private Integer point;
    @Transient
    private String cardTypeIdStr;
    @Transient
    private CardType cardTypeObject;
    private Date createTime;
    private String creator;
    private Boolean isPublic;//是否为总店发放的卡
    private Date startTime;
    private Date endTime;
    private String tagId;
    private String batchId;
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId createShopId;

    //-------------券参数-----------------------
    private ObjectId owner;//券所属会员的_id
    private String orderId;//使用后关联订单号
    private Date usedTime;//使用时间

    private String gainFrom;//获取途径

    /*public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }*/

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

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
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

    public String getCardTypeIdStr() {
        if(ValidatorUtil.isNotNull(this.cardTypeId)){
            return this.cardTypeId.toString();
        }
        return cardTypeIdStr;
    }

    public void setCardTypeIdStr(String cardTypeIdStr) {
        this.cardTypeIdStr = cardTypeIdStr;
    }

    public ObjectId getShopId() {
        return shopId;
    }

    public void setShopId(ObjectId shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
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

    public CardType getCardTypeObject() {
        return cardTypeObject;
    }

    public void setCardTypeObject(CardType cardTypeObject) {
        this.cardTypeObject = cardTypeObject;
    }

    public Integer getCardState() {
        return cardState;
    }

    public void setCardState(Integer cardState) {
        this.cardState = cardState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
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

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public ObjectId getCreateShopId() {
        return createShopId;
    }

    public void setCreateShopId(ObjectId createShopId) {
        this.createShopId = createShopId;
    }

    public ObjectId getOwner() {
        return owner;
    }

    public void setOwner(ObjectId owner) {
        this.owner = owner;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public String getGainFrom() {
        return gainFrom;
    }

    public void setGainFrom(String gainFrom) {
        this.gainFrom = gainFrom;
    }
}
