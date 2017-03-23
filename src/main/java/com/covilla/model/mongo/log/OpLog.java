package com.covilla.model.mongo.log;

import com.covilla.model.mongo.BaseModel;
import com.covilla.model.mongo.user.User;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * 操作Log对象
 * Created by qmaolong on 2017/2/10.
 */
public class OpLog extends BaseModel {
    private ObjectId shopId;
    private User operator;
    private Date time;
    private String description;
    private String remark;
    private String method;
    private String ip;
    private String operationItem;
    private Boolean success;
    private String failReason;

    public ObjectId getShopId() {
        return shopId;
    }

    public void setShopId(ObjectId shopId) {
        this.shopId = shopId;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getOperationItem() {
        return operationItem;
    }

    public void setOperationItem(String operationItem) {
        this.operationItem = operationItem;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
