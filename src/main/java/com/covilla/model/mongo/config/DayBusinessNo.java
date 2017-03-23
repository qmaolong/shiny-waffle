package com.covilla.model.mongo.config;

import java.util.List;

/**
 * Created by qmaolong on 2016/11/14.
 */
public class DayBusinessNo {
    private String name;
    private List<DayNo> values;

    private class DayNo {
        private String day;
        private Integer no;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public Integer getNo() {
            return no;
        }

        public void setNo(Integer no) {
            this.no = no;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DayNo> getValues() {
        return values;
    }

    public void setValues(List<DayNo> values) {
        this.values = values;
    }
}
