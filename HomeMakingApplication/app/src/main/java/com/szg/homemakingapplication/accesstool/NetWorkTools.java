package com.szg.homemakingapplication.accesstool;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.szg.homemakingapplication.MyApplication;
import com.szg.homemakingapplication.model.UserParentBean;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/17.
 */

public class NetWorkTools {
    public static String url="http://192.168.2.240:8080/exj/removte";
    //一、登录获取USER方法
    //用户名，密码，Callback
    public static void requestLog(String name, String pwd,Callback.CommonCallback<String> callback){
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("method","app/login/artistLogin");
        params.addBodyParameter("loginname",name);//普通post
        //String password= AES.encode(pwd);
        params.addBodyParameter("password",pwd);//普通post
        x.http().post(params,callback);
    }
    //四、获取竞单列表
    public static void requesFor_single_table(Callback.CommonCallback<String>callback){
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("method","order/getFightingOrderList");
        params.addBodyParameter("token",MyApplication.userBean.getToken());//普通post
        params.addBodyParameter("userid",MyApplication.userBean.getUserId()+"");//普通post
        x.http().post(params,callback);
    }
    //五、获取已抢单列表 1.页数 2.每页的数量
    public static void requestGrab_single_table(String pageNo,String pageSize,Callback.CommonCallback<String>callback){
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("method","order/getFoughtOrderList");
        params.addBodyParameter("token",MyApplication.userBean.getToken());//普通post
        params.addBodyParameter("userid",MyApplication.userBean.getUserId()+"");//普通post
        params.addBodyParameter("pageNo",pageNo);//普通post
        params.addBodyParameter("pageSize",pageSize);//普通post
        x.http().post(params,callback);
    }
    //六、获取订单列表  1.页数 2.每页的数量 3.订单状态 4.月份
    public static void requestOrderhistory(String pageNo,String pageSize,String status,String yMonth,Callback.CommonCallback<String> callback){
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("method","order/getMyOrderList");
        params.addBodyParameter("token",MyApplication.userBean.getToken());
        params.addBodyParameter("userid",MyApplication.userBean.getUserId()+"");
        params.addBodyParameter("pageNo",pageNo);
        params.addBodyParameter("pageSize",pageSize);
        params.addBodyParameter("status",status);
        params.addBodyParameter("yMonth",yMonth);
        x.http().post(params,callback);
    }
    //七、抢单 1.订单ID 2.价格 3.服务提供者ID
    public static void requestGrab_Single(String orderId,String price,String providerId,Callback.CommonCallback<String>callback){
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("method","order/grabOrder");
        params.addBodyParameter("token",MyApplication.userBean.getToken());
        params.addBodyParameter("orderId",orderId);
        params.addBodyParameter("price",price);
        params.addBodyParameter("providerId",providerId);
        x.http().post(params,callback);
    }
    //八、提交支付信息  1.订单号 2.服务数量 3.经度 4.纬度
    public static void requestSubmit_the_payment_information(String orderno,String confirmnum,String baiduMapLng,String baiduMapLat,Callback.CommonCallback<String>callback){
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("method","order/submitPayInfo");
        params.addBodyParameter("token",MyApplication.userBean.getToken());
        params.addBodyParameter("orderno",orderno);
        params.addBodyParameter("confirmnum",confirmnum);
        params.addBodyParameter("payType","2");
        params.addBodyParameter("baiduMapLng",baiduMapLng);
        params.addBodyParameter("baiduMapLat",baiduMapLat);
        x.http().post(params,callback);
    }
    //九、月收入总额 1.月份 如2016-09
    public static void requestTotal_monthly_income(String yMonth,Callback.CommonCallback<String>callback){
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("method","provider/getMyIncomeThisMonth");
        params.addBodyParameter("token",MyApplication.userBean.getToken());
        params.addBodyParameter("yMonth",yMonth);
        params.addBodyParameter("userid",MyApplication.userBean.getUserId()+"");
        x.http().post(params,callback);
    }

    //十、基本服务情况 1.月份 如2016-09
    public static void requestBasic_service(String yMonth,Callback.CommonCallback<String>callback){
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("method","provider/getMyServiceInfoThisMonth");
        params.addBodyParameter("token",MyApplication.userBean.getToken());
        params.addBodyParameter("yMonth",yMonth);
        params.addBodyParameter("userid",MyApplication.userBean.getUserId()+"");
        x.http().post(params,callback);
    }
    //十一、获取通知列表
    public static void requestGet_notification_list(Callback.CommonCallback<String>callback){
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("method","notice/searchNoticeList");
        params.addBodyParameter("token",MyApplication.userBean.getToken());
        params.addBodyParameter("userid",MyApplication.userBean.getUserId()+"");
        params.addBodyParameter("pageNo","1");
        params.addBodyParameter("pageSize","20");
        x.http().post(params,callback);
    }
    //十二、提交意见反馈 1.联系电话 2.内容
    public static void requestSubmit_feedback(String contact,String content,Callback.CommonCallback<String>callback){
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("method","feedback/saveFeedback");
        params.addBodyParameter("token",MyApplication.userBean.getToken());
        params.addBodyParameter("userid",MyApplication.userBean.getUserId()+"");
        params.addBodyParameter("nickname",java.net.URLEncoder.encode(MyApplication.userBean.getRealName()));
        params.addBodyParameter("contact",contact);
        params.addBodyParameter("content",content);
        params.addBodyParameter("feedbacktype","000100140002");
        x.http().post(params,callback);
    }
    //十三、修改密码 1.旧密码 2.新密码
    public static void requestChange_the_Password(String pwd,String oldpwd,Callback.CommonCallback<String>callback){
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("method","provider/saveEditPwd");
        params.addBodyParameter("token",MyApplication.userBean.getToken());
        params.addBodyParameter("userid",MyApplication.userBean.getUserId()+"");
        params.addBodyParameter("pwd",pwd);
        params.addBodyParameter("oldpwd",oldpwd);
        x.http().post(params,callback);
    }
    //十四、服务者坐标上传  1.是否在线 1在线0休息 2.纬度 3.经度
    public static void requestPosition_to_upload(String isonline,String baidulat,String baidulng,Callback.CommonCallback<String>callback){
        RequestParams params=new RequestParams(url);
        params.addBodyParameter("method","provider/uploadCoordinate");
        params.addBodyParameter("token",MyApplication.userBean.getToken());
        params.addBodyParameter("isonline",isonline);
        params.addBodyParameter("employeeid",MyApplication.userBean.getUserId()+"");
        params.addBodyParameter("baidulat",baidulat);
        params.addBodyParameter("baidulng",baidulng);
        x.http().post(params,callback);
    }
    //定位获得经纬度
    public static BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//              StringBuffer sb = new StringBuffer(256);
//				sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
//				sb.append(location.getTime());
//				sb.append("\nlatitude : ");// 纬度
                MyApplication.locationBean.setBaiduMapLat(location.getLatitude()+"");
//				sb.append(location.getLatitude());
//				sb.append("\nlontitude : ");// 经度
//				sb.append(location.getLongitude());
                MyApplication.locationBean.setBaiduMapLng(location.getLongitude()+"");
//				sb.append("\nCountry : ");// 国家名称
//				sb.append(location.getCountry());
//              sb.append("\ncity : ");// 城市
//              sb.append(location.getCity());
//				sb.append("\nDistrict : ");// 区
//				sb.append(location.getDistrict());
//				sb.append("\nStreet : ");// 街道
//				sb.append(location.getStreet());
//				sb.append("\naddr : ");// 地址信息
//				sb.append(location.getAddrStr());
//				logMsg(sb.toString());
//              sb.append("\nroctype:");
//              sb.append(location.getLocType());
            }
        }
    };
}
