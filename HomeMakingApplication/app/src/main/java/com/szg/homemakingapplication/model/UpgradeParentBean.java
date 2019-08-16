package com.szg.homemakingapplication.model;

/**
 * Created by Administrator on 2016/8/18.
 */

public class UpgradeParentBean {
    private String  code;
    private UpgradeBean data;
    private String  message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UpgradeBean getData() {
        return data;
    }

    public void setData(UpgradeBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
