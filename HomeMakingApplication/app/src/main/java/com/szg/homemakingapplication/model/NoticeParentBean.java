package com.szg.homemakingapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 */

public class NoticeParentBean implements Serializable {
    private String  code;
    private ArrayList<NoticeBean> data;
    private String  message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<NoticeBean> getData() {
        return data;
    }

    public void setData(ArrayList<NoticeBean> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
