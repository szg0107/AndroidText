package com.szg.homemakingapplication.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/17.
 */

public class NoticeBean implements Serializable {
    private  long noticeid;
    private  String	title;	//标题
    private  String	content;	//内容
    private  int	state;	//发布状态 0待发布1已发布
    private  String	createtime;	//创建时间
    private  String	pushtime;	//发布时间
    private  long		createid;		//对应系统用户表
    private  String	username;	//用户名

    public long getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(long noticeid) {
        this.noticeid = noticeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPushtime() {
        return pushtime;
    }

    public void setPushtime(String pushtime) {
        this.pushtime = pushtime;
    }

    public long getCreateid() {
        return createid;
    }

    public void setCreateid(long createid) {
        this.createid = createid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
