package com.extop.HangTianTianQiTong.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/14.
 */

public class CircleParentBean {
    private String  code;
    private ArrayList<CircleBean> data;
    private String  message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<CircleBean> getData() {
        return data;
    }

    public void setData(ArrayList<CircleBean> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
