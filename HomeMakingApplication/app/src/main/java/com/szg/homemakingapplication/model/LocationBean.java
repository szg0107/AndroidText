package com.szg.homemakingapplication.model;

/**
 * Created by Administrator on 2016/9/5.
 */

public class LocationBean {
    private  String	baiduMapLng;	//百度经度
    private  String	baiduMapLat;	//百度纬度

    public String getBaiduMapLng() {
        return baiduMapLng;
    }

    public void setBaiduMapLng(String baiduMapLng) {
        this.baiduMapLng = baiduMapLng;
    }

    public String getBaiduMapLat() {
        return baiduMapLat;
    }

    public void setBaiduMapLat(String baiduMapLat) {
        this.baiduMapLat = baiduMapLat;
    }
}
