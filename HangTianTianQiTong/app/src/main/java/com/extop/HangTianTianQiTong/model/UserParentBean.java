package com.extop.HangTianTianQiTong.model;

/**
 * Created by Administrator on 2017/3/7.
 */

public class UserParentBean {
    private String  code;
    private UserBean data;
    private String  message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserBean getData() {
        return data;
    }

    public void setData(UserBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
