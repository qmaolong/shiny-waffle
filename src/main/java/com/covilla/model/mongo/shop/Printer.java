package com.covilla.model.mongo.shop;

import java.util.Date;

/**
 * 打印机
 * Created by qmaolong on 2016/10/9.
 */
public class Printer {
    private String name;
    private Integer driverType;
    private String driverName;
    private Integer mode;
    private String driverId;
    private String kitchenForm;
    private String controlForm;
    private Integer copies;
    private Integer beep;
    private String errorForward;
    private String comPort;
    private Integer baudrate;
    private String ipAddress;
    private Boolean autoCut;
    private Boolean isDefault;
    private Date createTime;
    private String creator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDriverType() {
        return driverType;
    }

    public void setDriverType(Integer driverType) {
        this.driverType = driverType;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getKitchenForm() {
        return kitchenForm;
    }

    public void setKitchenForm(String kitchenForm) {
        this.kitchenForm = kitchenForm;
    }

    public String getControlForm() {
        return controlForm;
    }

    public void setControlForm(String controlForm) {
        this.controlForm = controlForm;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public Integer getBeep() {
        return beep;
    }

    public void setBeep(Integer beep) {
        this.beep = beep;
    }

    public String getErrorForward() {
        return errorForward;
    }

    public void setErrorForward(String errorForward) {
        this.errorForward = errorForward;
    }

    public String getComPort() {
        return comPort;
    }

    public void setComPort(String comPort) {
        this.comPort = comPort;
    }

    public Integer getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(Integer baudrate) {
        this.baudrate = baudrate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getAutoCut() {
        return autoCut;
    }

    public void setAutoCut(Boolean autoCut) {
        this.autoCut = autoCut;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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
}
