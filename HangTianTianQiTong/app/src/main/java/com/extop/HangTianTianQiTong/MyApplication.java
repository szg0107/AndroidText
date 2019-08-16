package com.extop.HangTianTianQiTong;

import android.app.Activity;
import android.app.Application;


import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2017/3/8.
 */

public class MyApplication extends Application {
    public static String circleID;//圈子ID
    public static String circleName;//圈子名称
    public static String userName;//用户名称
    public static String circleRZ;//圈子认证
    public static String userRZ;//用户认证
    public static String videoYZ;//视频认证
    public static String circleIcon;//圈子图标
    public static String isOpenSearch;//是否显示搜索圈子控件
    public static Boolean is_Virtual_key;//是否有虚拟键盘
    //    0:测试  1:正式  2:天企通
    public static int addressType = 2;
    public static String url = "";

//    public static String url="http://123.56.187.157/hlts/";/*测式地址*/
//    public static String url="http://www.extop.cn/"; /*正式地址*/
//    public static String url="http://106.74.152.27:8008/";/*天企通地址*/
    @Override
    public void onCreate() {
        super.onCreate();
        //注册xutils
        x.Ext.init(this);
        //初始化融云
        RongIM.init(this);
        //根据地址类型确定url
        switch (addressType) {
            case 0:
                url = "http://123.56.187.157/hlts/";/*测式地址*/
//                url="http://www.6xy8.com/hlts/";
                break;
            case 1:
                url = "http://www.extop.cn/"; /*正式地址*/
                break;
            case 2:
                url = "http://106.74.152.27:8008/";/*天企通地址*/
                break;
        }
    }


    /**
     * 添加到Activity容器中的参数、方法
     */

    public static List<Activity> activityList = new LinkedList<Activity>();

    /**
     * 添加到Activity容器中
     */
    public static void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * 便利所有Activigty并finish
     */
    public static void finishActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }

    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     */
    public static void finishSingleActivityByClass(Class<?> cls) {
        Activity tempActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }

        finishSingleActivity(tempActivity);
    }
}
