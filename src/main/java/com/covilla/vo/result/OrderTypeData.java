package com.covilla.vo.result;

import com.covilla.util.DateUtil;
import com.covilla.util.ValidatorUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by qmaolong on 2016/12/15.
 */
public class OrderTypeData {
    private String type;
    private String typeName;
    private Integer times;
    private BigDecimal amount;
    private String dateStr;

    public OrderTypeData(String type, String typeName, Integer times, BigDecimal amount, String dateStr) {
        this.type = type;
        this.typeName = typeName;
        this.times = times;
        this.amount = amount;
        this.dateStr = dateStr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public enum OrderTimeEnum{
        morning("凌晨0~6点", "00:00:00", "06:00:00"),
        day6to8("上午6~8点",  "06:00:00", "08:00:00"),
        day8to10("上午8~10点",  "08:00:00", "10:00:00"),
        day10to12("上午10~12点",  "10:00:00", "12:00:00"),
        day12to14("下午12~2点",  "12:00:00", "14:00:00"),
        day2to4("下午2~4点",  "14:00:00", "16:00:00"),
        day4to6("下午4~6点",  "16:00:00", "18:00:00"),
        night6to8("晚上6~8点",  "18:00:00", "20:00:00"),
        night8to10("晚上8~10点",  "20:00:00", "22:00:00"),
        night10to12("晚上10~12点",  "22:00:00", "24:00:00");
        private String name;
        private String startTimeStr;
        private String endTimeStr;
        private Date startDate;//日期转成当天
        private Date endDate;

        OrderTimeEnum(String name, String startTimeStr, String endTimeStr) {
            this.name = name;
            this.startTimeStr = startTimeStr;
            this.endTimeStr = endTimeStr;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartTimeStr() {
            return startTimeStr;
        }

        public void setStartTimeStr(String startTimeStr) {
            this.startTimeStr = startTimeStr;
        }

        public String getEndTimeStr() {
            return endTimeStr;
        }

        public void setEndTimeStr(String endTimeStr) {
            this.endTimeStr = endTimeStr;
        }

        public Date getStartDate() {
            if(ValidatorUtil.isNotNull(startTimeStr)){
                String today = DateUtil.formateDateToStr(new Date(), "yyyy-MM-dd");
                return DateUtil.formateStrToDate(today + startTimeStr, "yyyy-MM-ddHH:mm:ss");
            }
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            if(ValidatorUtil.isNotNull(endTimeStr)){
                String today = DateUtil.formateDateToStr(new Date(), "yyyy-MM-dd");
                return DateUtil.formateStrToDate(today + endTimeStr, "yyyy-MM-ddHH:mm:ss");
            }
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }
    }
}
