package com.szg.homemakingapplication.model;

/**
 * Created by Administrator on 2016/8/17.
 */

public class UpgradeBean {
    private  int	configid;	//配置ID
    private  String	configname;	//配置名称
    private  String	configtype;	//配置类型
    private  String[]	dataspare;	//升级信息的json对象
    private  String	updateCode;	//版本号
    private  String	updateInfo;	//升级内容
    private  String	updateSize;	//升级包大小
    private  int	updateForce;	//是否强制升级0：否 1：是

    public int getConfigid() {
        return configid;
    }

    public void setConfigid(int configid) {
        this.configid = configid;
    }

    public String getConfigname() {
        return configname;
    }

    public void setConfigname(String configname) {
        this.configname = configname;
    }

    public String getConfigtype() {
        return configtype;
    }

    public void setConfigtype(String configtype) {
        this.configtype = configtype;
    }

    public String[] getDataspare() {
        return dataspare;
    }

    public void setDataspare(String[] dataspare) {
        this.dataspare = dataspare;
    }

    public String getUpdateCode() {
        return updateCode;
    }

    public void setUpdateCode(String updateCode) {
        this.updateCode = updateCode;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getUpdateSize() {
        return updateSize;
    }

    public void setUpdateSize(String updateSize) {
        this.updateSize = updateSize;
    }

    public int getUpdateForce() {
        return updateForce;
    }

    public void setUpdateForce(int updateForce) {
        this.updateForce = updateForce;
    }
}
