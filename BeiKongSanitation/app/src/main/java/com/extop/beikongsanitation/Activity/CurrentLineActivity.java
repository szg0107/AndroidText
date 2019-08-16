package com.extop.beikongsanitation.Activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.extop.beikongsanitation.MainActivity;
import com.extop.beikongsanitation.MyApplication;
import com.extop.beikongsanitation.R;
import com.extop.beikongsanitation.Tool.NetWorkTools;
import com.extop.beikongsanitation.Tool.PublicActivity;

import org.json.JSONArray;

import org.json.JSONObject;
import org.xutils.common.Callback;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 * 当前线路
 */

public class CurrentLineActivity extends PublicActivity {
    private AMap aMap;//初始化地图控制器对象
    private MapView mapView;//地图控件
    private MyLocationStyle myLocationStyle;//目前为止类
    private List<DPoint> examplePoint = new ArrayList<DPoint>();//存储接口返回的坐标
    private Toolbar toolbar;
    //开始时间  结束时间  现在时间
    private long NowTimes,startTime,endTime;
    private Boolean isTask=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_line);
        //获取地图控件引用
        mapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);

        //获取Toolbar控件
        toolbar = findViewById(R.id.toolbar);
        //设置返回按钮并添加监听事件
        toolbar.setNavigationIcon(R.mipmap.arrow_left_d);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        //得到工作线路
        getRouteToWork();
        //得到当前位置
        getTheCurrentPosition();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }
    // 得到工作路线
    private void getRouteToWork(){
        //        Log.d("identificationCode",MyApplication.identificationCode);868861021207911
//        Log.d("identificationCode",MyApplication.carId);7
        NetWorkTools.GetRoadByWorkschedule(MyApplication.identificationCode, MyApplication.carId, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("返回的值", NetWorkTools.replaceBlank(result));
                try {
                    //获取全部返回值
                    JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                    //获取当前时间
                    Calendar calendar=Calendar.getInstance();
                    // 当前时间时分秒的秒数
                    NowTimes=calendar.get(Calendar.HOUR_OF_DAY)*3600+calendar.get(Calendar.MINUTE)*60+calendar.get(Calendar.SECOND);

                    for (int i = 0; i < (json.getJSONArray("data").length() - 2); i++) {
                        //路线上的坐标点数组清空
                        examplePoint.clear();
                        // 得到坐标数组
                        JSONArray data = json.getJSONArray("data").getJSONObject(i).getJSONArray("roadLatLng");
                        Log.d("data", data.toString());
                        Log.d("dataLength", data.length() + "");
                        //拿到任务时间区间并切割
                        String [] time=json.getJSONArray("data").getJSONObject(i).getString("time").split("-");
                        //将时分秒转换为秒数
                        startTime=stringToLong(time[0]);
                        endTime=stringToLong(time[1]);
                        // 如果在任务区间内绘制路线
                        if (startTime<=NowTimes&&NowTimes<=endTime){
                            for (int u = 0; u < data.length(); u++) {
                                examplePoint.add(new DPoint(data.getJSONObject(u).getDouble("startlat"),data.getJSONObject(u).getDouble("startlng")));
                                examplePoint.add(new DPoint(data.getJSONObject(u).getDouble("endlat"), data.getJSONObject(u).getDouble("endtlng")));
                            }
                            addPolyLinesSolid();
                            isTask=true;
                        }
                    }
                    Log.d("开始时间",startTime+"");
                    Log.d("结束时间",endTime+"");
                    Log.d("现在时间",NowTimes+"");
                    if(!isTask) {
//                            Toast.makeText(CurrentLineActivity.this,"您目前还没有工作任务",Toast.LENGTH_LONG);
                        AlertDialog.Builder builder = new AlertDialog.Builder(CurrentLineActivity.this, R.style.AlertDialog);
                        builder.setMessage("您目前还没有工作任务!");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setCancelable(false);
                        builder.create().show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CurrentLineActivity.this,"路线数据错误:"+e,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("错误", ex + "");
                Toast.makeText(CurrentLineActivity.this,"得到路线失败:"+ex,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //绘制一条实线
    private void addPolyLinesSolid () {
        List<LatLng> latLng = NetWorkTools.convert(examplePoint, getApplicationContext());
        aMap.addPolyline((new PolylineOptions())
                .addAll(latLng)
                .width(10)
                .color(Color.argb(255, 1, 255, 255)));
    }

    // 字符串时分秒转换为秒数
    private  Long stringToLong(String strTime) {
        String [] strTimes=strTime.split(":");
        long s=Integer.parseInt(strTimes[0])*3600;    //小时
        s+=Integer.parseInt(strTimes[1])*60;    //分钟
        s+=Integer.parseInt(strTimes[2]);    //秒
        return s;
    }

    // 得到当前位置
    private void getTheCurrentPosition(){
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }


    private void setUpMap() {
        /*按照传入的CameraUpdate参数改变地图状态。（直接改变状态，没有动画效果）
        可以通过CameraUpdateFactory.zoomIn()等方法来生成对应的CameraUpdate对象。*/
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.300299, 106.347656), 4));
        //        设置地图底图文字标注的层级指数，默认为0，用来比较覆盖物（polyline、polygon、circle等）的zIndex。
//        aMap.setMapTextZIndex(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }
}
