package com.extop.beikongsanitation;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2018/1/18.
 */

public class MyApplication extends Application {
    //环卫需要记录的变量
    public static String driverId;//司机ID
    public static String driverName;//司机名称
    public static String driverCode;//工号
    public static String carId;//汽车id
    public static String loginId;//登录id
    public static String carType;//汽车类型
    public static String Id;//目前不知道干嘛的
    public static String operateId;//操作id
    public static Boolean typeStatus;//是否是有垃圾回收功能
    public static String carNum; //车牌号

    //自己需要记录的变量
    public static String identificationCode;//IMEI
    public static String times;//时间
    public static String address;//地址
    /*按钮功能类型 0 默认无意义 1 加水 2 倾倒 3 排污 4 加水完毕 5 倾倒完毕 6 排污完毕*/
    public static int operateType=0;//按钮功能类型
    public static Boolean isClick = true;// 加水、倾倒、排污是否可点击
    @Override
    public void onCreate() {
        super.onCreate();
        //注册xutils
        x.Ext.init(this);
    }
}
