package com.szg.homemakingapplication.model;

/**
 * Created by Administrator on 2016/9/11.
 */

public class BasicServiceParentBean {
    private String code;
    private BasicServiceBean data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BasicServiceBean getData() {
        return data;
    }

    public void setData(BasicServiceBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
