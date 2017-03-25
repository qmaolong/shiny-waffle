package com.covilla.vo.filter;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * 数据统计筛选项
 * Created by qmaolong on 2016/11/9.
 */
public class QueryFilter {
    private String moduleName;//模板名称
    private String dataType;//统计数据类型
    private Date startDate;
    private Date endDate;
    private String payment;
    private String operator;
    private String dayOn;
    private String dayOff;
    private String shopId;
    private List<ObjectId> shopIds;//当shopId为空时，用shopIds查询

    //卡券统计数据
    private Boolean isCoupon;
    private Boolean isPublic;//单店卡or总店卡

    private String period;
    private Integer specialTimeOn;
    private Integer specialTimeOff;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getSpecialTimeOn() {
        return specialTimeOn;
    }

    public void setSpecialTimeOn(Integer specialTimeOn) {
        this.specialTimeOn = specialTimeOn;
    }

    public Integer getSpecialTimeOff() {
        return specialTimeOff;
    }

    public void setSpecialTimeOff(Integer specialTimeOff) {
        this.specialTimeOff = specialTimeOff;
    }

    public static enum PeriodEnum {
        year("year", "年"),
        month("month", "月"),
        week("week", "周");
        private String name;
        private String desc;

        PeriodEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<ObjectId> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<ObjectId> shopIds) {
        this.shopIds = shopIds;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(Boolean coupon) {
        isCoupon = coupon;
    }
}
