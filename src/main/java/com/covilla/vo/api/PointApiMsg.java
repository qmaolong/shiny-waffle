package com.covilla.vo.api;

import com.covilla.vo.BaseApiResultMsg;

/**
 * Created by qmaolong on 2017/2/28.
 */
public class PointApiMsg extends BaseApiResultMsg {
    private String seriesNo;
    private Integer lastPoint;
    private Integer curPoint;

    public PointApiMsg(String seriesNo, Integer lastPoint, Integer curPoint) {
        this.seriesNo = seriesNo;
        this.lastPoint = lastPoint;
        this.curPoint = curPoint;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public Integer getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(Integer lastPoint) {
        this.lastPoint = lastPoint;
    }

    public Integer getCurPoint() {
        return curPoint;
    }

    public void setCurPoint(Integer curPoint) {
        this.curPoint = curPoint;
    }
}
