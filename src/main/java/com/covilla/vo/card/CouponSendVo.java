package com.covilla.vo.card;

import com.covilla.plugin.serializer.ObjectIdDeSerializer;
import com.covilla.plugin.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by qmaolong on 2017/2/19.
 */
public class CouponSendVo {
    @JsonDeserialize(using = ObjectIdDeSerializer.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId couponTypeId;
    private Integer count;
    private Date expired;

    public ObjectId getCouponTypeId() {
        return couponTypeId;
    }

    public void setCouponTypeId(ObjectId couponTypeId) {
        this.couponTypeId = couponTypeId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}
