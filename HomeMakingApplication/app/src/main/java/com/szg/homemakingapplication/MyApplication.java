package com.szg.homemakingapplication;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.szg.homemakingapplication.location.LocationService;
import com.szg.homemakingapplication.model.LocationBean;
import com.szg.homemakingapplication.model.UserBean;

import org.xutils.x;

/**
 * Created by Administrator on 2016/8/18.
 */

public class MyApplication extends Application {
    public static UserBean userBean;
    public static LocationService locationService;
    public static LocationBean locationBean;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        locationBean=new LocationBean();
        SDKInitializer.initialize(getApplicationContext());
    }
}
