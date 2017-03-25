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
import java.util.List;

@Document
public class CardType extends BaseModel {
    @Field("id")
    private Integer id;

    private String name;

    private String cardDesc;

    private Boolean isPublic;//总店参数，配置是否为所有点通用

    private String coverImg;

    private Boolean isSupportCharge = false;

    private Boolean isSupportDiscount = false;

    private Boolean isSupportPoint = false;

    private Boolean usePricem;

    private Integer fitType;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId owner;

    private Integer count;

    private Date expiredDate;

    private Date createTime;
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId creator;
    @Field("chargeRules")
    private List<CardChargeRule> chargeRules;
    @Field("discountRules")
    private List<CardDiscountRule> discountRules;

    private BigDecimal initBalance;
    @Transient
    private Integer couponType;//现金or折扣

    private Boolean isCoupon;

    private Boolean wxSupport;//是否为默认微会员类型

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

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public Boolean getIsSupportCharge() {
        return isSupportCharge;
    }

    public void setIsSupportCharge(Boolean isSupportCharge) {
        this.isSupportCharge = isSupportCharge;
    }

    public Boolean getIsSupportDiscount() {
        return isSupportDiscount;
    }

    public void setIsSupportDiscount(Boolean isSupportDiscount) {
        this.isSupportDiscount = isSupportDiscount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ObjectId getCreator() {
        return creator;
    }

    public void setCreator(ObjectId creator) {
        this.creator = creator;
    }

    public List<CardChargeRule> getChargeRules() {
        return chargeRules;
    }

    public void setChargeRules(List<CardChargeRule> chargeRules) {
        this.chargeRules = chargeRules;
    }

    public List<CardDiscountRule> getDiscountRules() {
        return discountRules;
    }

    public void setDiscountRules(List<CardDiscountRule> discountRules) {
        this.discountRules = discountRules;
    }

    public Boolean getUsePricem() {
        return usePricem;
    }

    public void setUsePricem(Boolean usePricem) {
        this.usePricem = usePricem;
    }

    public Integer getFitType() {
        return fitType;
    }

    public void setFitType(Integer fitType) {
        this.fitType = fitType;
    }

    public Boolean getIsSupportPoint() {
        return isSupportPoint;
    }

    public void setIsSupportPoint(Boolean supportPoint) {
        isSupportPoint = supportPoint;
    }

    public ObjectId getOwner() {
        return owner;
    }

    public void setOwner(ObjectId owner) {
        this.owner = owner;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getSupport() {
        String result = "";
        if (ValidatorUtil.isNotNull(this.isSupportCharge) && this.isSupportCharge) {
            result += "储值、";
        }
        if (ValidatorUtil.isNotNull(this.isSupportDiscount) && this.isSupportDiscount) {
            result += "优惠、";
        }
        if (ValidatorUtil.isNotNull(this.isSupportPoint) && this.isSupportPoint) {
            result += "积分、";
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public BigDecimal getInitBalance() {
        return initBalance;
    }

    public void setInitBalance(BigDecimal initBalance) {
        this.initBalance = initBalance;
    }

    public Integer getCouponType() {
        if(ValidatorUtil.isNull(discountRules)){
            return 0;
        }else {
            return 1;
        }
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Boolean getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(Boolean coupon) {
        isCoupon = coupon;
    }

    public Boolean getWxSupport() {
        return wxSupport;
    }

    public void setWxSupport(Boolean wxSupport) {
        this.wxSupport = wxSupport;
    }
}