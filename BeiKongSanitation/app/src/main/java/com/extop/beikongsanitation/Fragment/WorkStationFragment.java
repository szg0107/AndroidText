package com.extop.beikongsanitation.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.extop.beikongsanitation.MyApplication;
import com.extop.beikongsanitation.R;
import com.extop.beikongsanitation.Tool.NetWorkTools;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**操作台**/
public class WorkStationFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    // 加水 加水完毕 倾倒  倾倒完毕 排污 排污完毕 垃圾收运 当前线路 线路更新 紧急清运呼叫
    private Button addWater, waterComplete, dumping, dumpingComplete, sewage, sewageComplete,garbageCollector, currentLine, routeUpdate, callClearance;
    // 加水、倾倒、排污是否可点击
//    private Boolean isClick = true;
    // 司机名 工号 登录时间 登录地址 车类型 车牌号
    private TextView driverName,driverCode,times,address,carType,carNum;
    /*按钮功能类型
    0 默认无意义
    1 加水
    2 倾倒
    3 排污
    4 加水完毕
    5 倾倒完毕
    6 排污完毕
    */
//    private int operateType=0;
    //跳转页面对象
    private Intent intent;

    public static WorkStationFragment newInstance(String param1) {
        WorkStationFragment fragment = new WorkStationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_station, container, false);
        //司机名
        driverName=view.findViewById(R.id.driverName);
        driverName.setText(MyApplication.driverName);
        //工号
        driverCode=view.findViewById(R.id.driverCode);
        driverCode.setText(MyApplication.driverCode);
        //登录时间
        times=view.findViewById(R.id.times);
        times.setText(MyApplication.times);
        //登录地址
        address=view.findViewById(R.id.address);
        address.setText(MyApplication.address);
        //车类型
        carType=view.findViewById(R.id.carType);
        carType.setText(MyApplication.carType);
        //车牌号
        carNum=view.findViewById(R.id.carNum);
        carNum.setText(MyApplication.carNum);
        //加水
        addWater = view.findViewById(R.id.addWater);
        addWater.setOnClickListener(this);
        //加水完毕
        waterComplete = view.findViewById(R.id.waterComplete);
        waterComplete.setOnClickListener(this);
        // 倾倒
        dumping = view.findViewById(R.id.dumping);
        dumping.setOnClickListener(this);
        //倾倒完毕
        dumpingComplete = view.findViewById(R.id.dumpingComplete);
        dumpingComplete.setOnClickListener(this);
        // 排污
        sewage = view.findViewById(R.id.sewage);
        sewage.setOnClickListener(this);
        // 排污完毕
        sewageComplete = view.findViewById(R.id.sewageComplete);
        sewageComplete.setOnClickListener(this);
        //垃圾收运
        garbageCollector=view.findViewById(R.id.garbageCollector);
        garbageCollector.setOnClickListener(this);
        if (MyApplication.typeStatus){
            garbageCollector.setVisibility(View.VISIBLE);
        }
        // 当前线路
        currentLine = view.findViewById(R.id.currentLine);
        currentLine.setOnClickListener(this);
        // 线路更新
        routeUpdate = view.findViewById(R.id.routeUpdate);
        routeUpdate.setOnClickListener(this);
        //紧急清运呼叫
        callClearance = view.findViewById(R.id.callClearance);
        callClearance.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext().getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 定位回调
        mLocationListener= new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.d("定位结果", aMapLocation.toString());
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        //获取定位时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);
                        Log.d("信息", aMapLocation.getLatitude()+","
                                +aMapLocation.getLongitude()+","
                                +aMapLocation.getProvince()+","
                                +aMapLocation.getCity()+","
                                +aMapLocation.getDistrict()+","
                                +aMapLocation.getStreet()+","
                                +aMapLocation.getStreetNum()+","
                                +df.format(date)
                        );
                        switch (MyApplication.operateType){
                            case 1:
                            case 2:
                            case 3:
                                AddRecordDetail(MyApplication.operateType+"",aMapLocation.getLatitude()+"",aMapLocation.getLongitude()+"");
                                break;
                            case 4:
                            case 5:
                            case 6:
                                UpdateRecordDetail(aMapLocation.getLatitude()+"",aMapLocation.getLongitude()+"");
                                break;
                        }
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }
    /*按钮功能类型  0 全部 1加水 2 倾倒  3 排污  4 加水完毕 5 倾倒完毕  6 排污完毕*/
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.addWater:
                //加水
                if (MyApplication.operateType==0){
                    MyApplication.operateType=1;
                    mLocationClient.startLocation();
                    Toast.makeText(getContext(),"开始加水。",Toast.LENGTH_SHORT).show();
                }
                if (!MyApplication.isClick&&MyApplication.operateType==1){
                    Toast.makeText(getContext(),"正在加水,请勿重复操作!",Toast.LENGTH_LONG).show();
                }
                if(MyApplication.operateType!=1){
                    Toast.makeText(getContext(),"您正在进行其他操作，不能进行加水操作！",Toast.LENGTH_LONG).show();
                }
                MyApplication.isClick=false;
                break;
            case R.id.waterComplete:
                //加水完毕
                if (MyApplication.operateType==1){
                    MyApplication.operateType=4;
                    mLocationClient.startLocation();
                    Toast.makeText(getContext(),"加水完毕！",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(),"您当前没有在进行加水操作，不能进行加水完毕操作！！！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.dumping:
                //倾倒
                if (MyApplication.operateType==0){
                    MyApplication.operateType=2;
                    mLocationClient.startLocation();
                    Toast.makeText(getContext(),"开始倾倒。",Toast.LENGTH_SHORT).show();
                }
                if (!MyApplication.isClick&&MyApplication.operateType==2){
                    Toast.makeText(getContext(),"正在倾倒,请勿重复操作!",Toast.LENGTH_LONG).show();
                }
                if (MyApplication.operateType!=2){
                    Toast.makeText(getContext(),"您正在进行其他操作，不能进行倾倒操作！",Toast.LENGTH_LONG).show();
                }
                MyApplication.isClick=false;
                break;
            case R.id.dumpingComplete:
                //倾倒完毕
                if (MyApplication.operateType==2){
                    MyApplication.operateType=5;
                    mLocationClient.startLocation();
                    Toast.makeText(getContext(),"倾倒完毕！",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(),"您当前没有在进行倾倒操作，不能进行倾倒完毕操作！！！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.sewage:
                //排污
                if (MyApplication.operateType==0){
                    MyApplication.operateType=3;
                    mLocationClient.startLocation();
                    Toast.makeText(getContext(),"开始排污。",Toast.LENGTH_SHORT).show();
                }
                if (!MyApplication.isClick&&MyApplication.operateType==3){
                    Toast.makeText(getContext(),"正在排污请,勿重复操作!",Toast.LENGTH_LONG).show();
                }
                if(MyApplication.operateType!=3) {
                    Toast.makeText(getContext(),"您正在进行其他操作，不能进行排污操作！",Toast.LENGTH_LONG).show();
                }
                MyApplication.isClick=false;
                break;
            case R.id.sewageComplete:
                //排污完毕
                if (MyApplication.operateType==3){
                    MyApplication.operateType=6;
                    mLocationClient.startLocation();
                    Toast.makeText(getContext(),"排污完毕！",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(),"您当前没有在进行排污操作，不能进行排污完毕操作！！！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.garbageCollector:
                //垃圾收运
                intent = new Intent("android.intent.Activity.AddGarbageCollectorActivity");
                startActivity(intent);
                break;
            case R.id.currentLine:
                //当前线路
                intent = new Intent("android.intent.Activity.CurrentLineActivity");
                startActivity(intent);
                break;
            case R.id.routeUpdate:
                //线路更新
                intent = new Intent("android.intent.Activity.CurrentLineActivity");
                startActivity(intent);
                break;
            case R.id.callClearance:
                //紧急清运呼叫
                Toast.makeText(getContext(),"很抱歉，当前版本不支持紧急清运呼叫功能！",Toast.LENGTH_LONG).show();
                break;
        }
    }

    /*添加操作记录*/
    private void AddRecordDetail(String operateTypeId,String lat,String lng){
        NetWorkTools.AddRecordDetail(MyApplication.identificationCode, MyApplication.carId, MyApplication.driverId, operateTypeId, lat, lng, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("返回的值", NetWorkTools.replaceBlank(result));
                try {
                    //获取全部返回值
                    JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                    JSONObject data = json.getJSONArray("data").getJSONObject(0);
                    MyApplication.operateId=data.getString("operateId");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("错误", ex + "");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /*更新操作记录*/
    private void UpdateRecordDetail(String lat,String lng){
        NetWorkTools.UpdateRecordDetail(MyApplication.identificationCode, MyApplication.operateId, lat, lng, new Callback.CommonCallback<String>() {            @Override
        public void onSuccess(String result) {
            Log.d("返回的值", NetWorkTools.replaceBlank(result));
            try {
                //获取全部返回值
                JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                JSONObject data = json.getJSONArray("data").getJSONObject(0);
                MyApplication.operateId=data.getString("operateId");
                MyApplication.operateType=0;
                MyApplication.isClick=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("错误", ex + "");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
