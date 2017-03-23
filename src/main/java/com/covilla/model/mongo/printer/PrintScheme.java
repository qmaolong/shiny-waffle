package com.covilla.model.mongo.printer;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * 厨打方案
 * Created by qmaolong on 2016/11/2.
 */
public class PrintScheme {
    private String name;
    private Integer type;
    private List<RuleItem> items;

    public static  class RuleItem {
        @Field("id")
        private Integer id;
        private String name;
        private String printer1;
        private String printer2;
        private String printer3;
        private String printerControl;
        private String remark;

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

        public String getPrinter1() {
            return printer1;
        }

        public void setPrinter1(String printer1) {
            this.printer1 = printer1;
        }

        public String getPrinter2() {
            return printer2;
        }

        public void setPrinter2(String printer2) {
            this.printer2 = printer2;
        }

        public String getPrinter3() {
            return printer3;
        }

        public void setPrinter3(String printer3) {
            this.printer3 = printer3;
        }

        public String getPrinterControl() {
            return printerControl;
        }

        public void setPrinterControl(String printerControl) {
            this.printerControl = printerControl;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<RuleItem> getItems() {
        return items;
    }

    public void setItems(List<RuleItem> items) {
        this.items = items;
    }
}
