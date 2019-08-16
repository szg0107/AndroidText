package com.extop.HangTianTianQiTong.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/14.
 * 存放JSON对象目前model处于废弃状态
 */

public class CircleBean implements Serializable {
    String iconURL ;
    String circleName;
    String circleId;

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }
}
