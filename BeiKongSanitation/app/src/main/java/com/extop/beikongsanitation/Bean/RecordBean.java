package com.extop.beikongsanitation.Bean;

/**
 * Created by Administrator on 2018/1/26.
 */

public class RecordBean {
    private String startTime;
    private String startTimeValue;
    private String startPosition;
    private String startPositionValue;
    private String endTime;
    private String endTimeValue;
    private String endPosition;
    private String endPositionValue;
    //无参数
    public RecordBean() {
    }
    //六个参数
    public RecordBean(String startTime, String startTimeValue, String startPosition, String startPositionValue, String endTime, String endTimeValue) {
        this.startTime = startTime;
        this.startTimeValue = startTimeValue;
        this.startPosition = startPosition;
        this.startPositionValue = startPositionValue;
        this.endTime = endTime;
        this.endTimeValue = endTimeValue;
    }
    //八个参数
    public RecordBean(String startTime, String startTimeValue, String startPosition, String startPositionValue, String endTime, String endTimeValue, String endPosition, String endPositionValue) {
        this.startTime = startTime;
        this.startTimeValue = startTimeValue;
        this.startPosition = startPosition;
        this.startPositionValue = startPositionValue;
        this.endTime = endTime;
        this.endTimeValue = endTimeValue;
        this.endPosition = endPosition;
        this.endPositionValue = endPositionValue;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeValue() {
        return startTimeValue;
    }

    public void setStartTimeValue(String startTimeValue) {
        this.startTimeValue = startTimeValue;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getStartPositionValue() {
        return startPositionValue;
    }

    public void setStartPositionValue(String startPositionValue) {
        this.startPositionValue = startPositionValue;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeValue() {
        return endTimeValue;
    }

    public void setEndTimeValue(String endTimeValue) {
        this.endTimeValue = endTimeValue;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public String getEndPositionValue() {
        return endPositionValue;
    }

    public void setEndPositionValue(String endPositionValue) {
        this.endPositionValue = endPositionValue;
    }
}
