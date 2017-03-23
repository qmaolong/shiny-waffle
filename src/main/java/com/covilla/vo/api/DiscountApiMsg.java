package com.covilla.vo.api;

import com.covilla.vo.BaseApiResultMsg;

/**
 * Created by qmaolong on 2016/11/18.
 */
public class DiscountApiMsg extends BaseApiResultMsg {
    private String seriesNumber;

    public DiscountApiMsg(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }
}
