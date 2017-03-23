package com.covilla.model.mongo.card;

import com.covilla.model.mongo.BaseModel;
import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.covilla.vo.card.CouponSendVo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2016/11/15.
 */
public class CardBatch extends BaseModel {
    @Field("id")
    private String id;
    private String name;
    private Double balance;
    private Integer medium;
    private Integer noLength;
    private String prefix;
    private Integer startNo;
    private Integer endNo;
    private Date startTime;
    private Date endTime;
    private Boolean isPublic;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId cardTypeId;
    private Integer count;
    private String cardTypeName;
    private Date createTime;
    private String creator;
    @Transient
    private String cardTypeIdStr;
    private Integer CardState;
    private String specialSuffix;//过滤特殊尾号

    //批量送券给微会员参数
    private List<CouponSendVo> couponSendVos;
    private Integer fitMemberCount;

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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public ObjectId getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(ObjectId cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getNoLength() {
        return noLength;
    }

    public void setNoLength(Integer noLength) {
        this.noLength = noLength;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getStartNo() {
        return startNo;
    }

    public void setStartNo(Integer startNo) {
        this.startNo = startNo;
    }

    public Integer getEndNo() {
        return endNo;
    }

    public void setEndNo(Integer endNo) {
        this.endNo = endNo;
    }

    public String getCardTypeIdStr() {
        return cardTypeIdStr;
    }

    public void setCardTypeIdStr(String cardTypeIdStr) {
        this.cardTypeIdStr = cardTypeIdStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMedium() {
        return medium;
    }

    public void setMedium(Integer medium) {
        this.medium = medium;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getCardState() {
        return CardState;
    }

    public void setCardState(Integer cardState) {
        CardState = cardState;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSpecialSuffix() {
        return specialSuffix;
    }

    public void setSpecialSuffix(String specialSuffix) {
        this.specialSuffix = specialSuffix;
    }

    public List<CouponSendVo> getCouponSendVos() {
        return couponSendVos;
    }

    public void setCouponSendVos(List<CouponSendVo> couponSendVos) {
        this.couponSendVos = couponSendVos;
    }

    public Integer getFitMemberCount() {
        return fitMemberCount;
    }

    public void setFitMemberCount(Integer fitMemberCount) {
        this.fitMemberCount = fitMemberCount;
    }
}
