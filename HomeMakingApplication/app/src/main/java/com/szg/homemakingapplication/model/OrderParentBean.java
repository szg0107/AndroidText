package com.szg.homemakingapplication.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 */

public class OrderParentBean {

    private String  code;
    private ArrayList<OrderBean> data;
    private String  message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<OrderBean> getData() {
        return data;
    }

    public void setData(ArrayList<OrderBean> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
