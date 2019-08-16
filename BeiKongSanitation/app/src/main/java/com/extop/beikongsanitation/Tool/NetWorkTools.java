package com.extop.beikongsanitation.Tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.model.LatLng;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/17.
 */

public class NetWorkTools {
    public static String url = "http://221.195.75.175:7080/dezhou_server/";

    //一、登录获取USER方法
    //登录名，工号，平板设备号，纬度，经度，Callback
    public static void getLoginFlatByDriver(String driverName, String driverCode, String imei, String lat, String lng, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "GetLoginFlatByDriver");
        params.addBodyParameter("driverName", driverName);//登录名
        params.addBodyParameter("driverCode", driverCode);//工号
        params.addBodyParameter("imei", imei);//平板设备号
        params.addBodyParameter("lat", lat);//纬度
        params.addBodyParameter("lng", lng);//经度
        x.http().get(params, callback);
    }

    //二、登出 平板设备号，纬度，经度，登录id
    public static void OutLoginFlatByDriver(String imei, String lat, String lng, String loginId, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "OutLoginFlatByDriver");
        params.addBodyParameter("imei", imei);//平板设备号
        params.addBodyParameter("lat", lat);//纬度
        params.addBodyParameter("lng", lng);//经度
        params.addBodyParameter("loginId", loginId);//登录id
        x.http().get(params, callback);
    }

    //三、获取车辆类型 平板设备号，车辆id
    public static void GetCarTypeByCarId(String imei, String carId, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "GetCarTypeByCarId");
        params.addBodyParameter("imei", imei);//平板设备号
        params.addBodyParameter("carId", carId);//车辆id
        x.http().get(params, callback);
    }

    //四、获取路线 平板设备号，车辆id
    public static void GetRoadByWorkschedule(String imei, String carId, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "GetRoadByWorkschedule");
        params.addBodyParameter("imei", imei);//平板设备号
        params.addBodyParameter("carId", carId);//车辆id
        x.http().get(params, callback);
    }

    //五、操作功能开启 平板设备号 车辆id 司机id 操作类型id(1.加水 2.倾倒 3.排污) 纬度 经度
    public static void AddRecordDetail(String imei, String carId, String driverId,String operateTypeId,String lat,String lng,Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "AddRecordDetail");
        params.addBodyParameter("imei", imei);//平板设备号
        params.addBodyParameter("carId", carId);//车辆id
        params.addBodyParameter("driverId", driverId);//司机id
        params.addBodyParameter("operatetypeId", operateTypeId);//操作类型id
        params.addBodyParameter("lat", lat);//纬度
        params.addBodyParameter("lng", lng);//经度
        x.http().get(params, callback);
    }

    //五、垃圾收运功能开启 平板设备号 车辆id 司机id 操作类型id(1.加水 2.倾倒 3.排污) 纬度 经度 垃圾编号 满度
    public static void AddRecordDetail_GarbageCollector(String imei, String carId, String driverId,String operateTypeId,String lat,String lng,String code,String fullScale,Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "AddRecordDetail");
        params.addBodyParameter("imei", imei);//平板设备号
        params.addBodyParameter("carId", carId);//车辆id
        params.addBodyParameter("driverId", driverId);//司机id
        params.addBodyParameter("operatetypeId", operateTypeId);//操作类型id
        params.addBodyParameter("lat", lat);//纬度
        params.addBodyParameter("lng", lng);//经度
        params.addBodyParameter("code", code);//垃圾编号
        params.addBodyParameter("fullscale", fullScale);//满度
        x.http().get(params, callback);
    }

    //六、操作功能关闭 平板设备号 操作类型id(1.加水 2.倾倒 3.排污) 纬度 经度
    public static void UpdateRecordDetail(String imei, String operateId, String lat,String lng,Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "UpdateRecordDetail");
        params.addBodyParameter("imei", imei);//平板设备号
        params.addBodyParameter("operateId", operateId);//车辆id
        params.addBodyParameter("lat", lat);//纬度
        params.addBodyParameter("lng", lng);//经度
        x.http().get(params, callback);
    }

    //七、获取登录明细 平板设备号 车辆id 司机id
    public static void GetLoginFlatDetail(String imei, String carId, String driverId,Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "GetLoginFlatDetail");
        params.addBodyParameter("imei", imei);//平板设备号
        params.addBodyParameter("carId", carId);//车辆id
        params.addBodyParameter("driverId", driverId);//司机ID
        x.http().get(params, callback);
    }

    //八、获取操作明细 平板设备号 车辆id 司机id 操作id 1:加水 2:倾倒 3:排污 4:垃圾收运
    public static void GetRecordDetail(String imei, String carId, String driverId,String operateTypeId,Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "GetRecordDetail");
        params.addBodyParameter("imei", imei);//平板设备号
        params.addBodyParameter("carId", carId);//车辆id
        params.addBodyParameter("driverId", driverId);//司机ID
        params.addBodyParameter("operatetypeId", operateTypeId);//操作id
        x.http().get(params, callback);
    }

    //九、获取版本信息 平板设备号
    public static void GetVersion(String imei,Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "GetVersion");
        params.addBodyParameter("imei", imei);//平板设备号
        x.http().get(params, callback);
    }

    //java去除字符串中的空格、回车、换行符、制表符
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    //将图片URL转换为Bitmap
    public static Bitmap getURLImage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }


    //高德坐标转换
    public static List<LatLng> convert(List<DPoint> examplePoint, Context context) {
        //构造一个示例坐标，第一个参数是lat纬度，第二个参数是lng经度
        //	DPoint examplePoint = new DPoint(36.397755, 120.430336);
        List<LatLng> latLngs = new ArrayList<LatLng>();
        try {
            //初始化坐标转换类
            CoordinateConverter converter = new CoordinateConverter(
                    context);

            /**
             * 设置坐标来源,这里使用百度坐标作为示例
             * 可选的来源包括：
             * <li>CoordType.BAIDU ： 百度坐标
             * <li>CoordType.MAPBAR ： 图吧坐标
             * <li>CoordType.MAPABC ： 图盟坐标
             * <li>CoordType.SOSOMAP ： 搜搜坐标
             * <li>CoordType.ALIYUN ： 阿里云坐标
             * <li>CoordType.GOOGLE ： 谷歌坐标
             * <li>CoordType.GPS ： GPS坐标
             */
            converter.from(CoordinateConverter.CoordType.GPS);
            for (int i = 0; i < examplePoint.size(); i++) {
                //设置需要转换的坐标
                converter.coord(examplePoint.get(i));
                //转换成高德坐标
                DPoint destPoint = converter.convert();
                if (null != destPoint) {
                    //判断坐标是否高德地图可用
//                    Toast.makeText(context, +destPoint.getLatitude() + "" + destPoint.getLongitude(), Toast.LENGTH_SHORT).show();
                    boolean result = converter.isAMapDataAvailable(destPoint.getLatitude(), destPoint.getLongitude());
                    if (result) {
//                        Toast.makeText(context, "该坐标是高德地图可用坐标", Toast.LENGTH_SHORT).show();
                        latLngs.add(new LatLng(destPoint.getLatitude(), destPoint.getLongitude()));
                    } else {
//                        Toast.makeText(context, "该坐标不能用于高德地图", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "坐标转换失败", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(context, "坐标转换失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return latLngs;
    }


}
