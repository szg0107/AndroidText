package com.extop.beikongsanitation.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.extop.beikongsanitation.MyApplication;
import com.extop.beikongsanitation.R;
import com.extop.beikongsanitation.Tool.NetWorkTools;
import com.extop.beikongsanitation.Tool.PublicActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/31.
 * 添加垃圾收运
 */

public class AddGarbageCollectorActivity extends PublicActivity {
    // 垃圾箱编号 满度
    EditText code,fullScale;
    //提交按钮
    Button determine;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_collector);
        code=findViewById(R.id.code);
        fullScale=findViewById(R.id.fullScale);
        determine=findViewById(R.id.determine);

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
        determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动定位
                if (code.length() > 0 && fullScale.length() > 0) {
                    mLocationClient.startLocation();
                } else {
                    Toast.makeText(AddGarbageCollectorActivity.this, "垃圾箱编号或满度不能为空,请输入！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 定位回调
        mLocationListener = new AMapLocationListener() {
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
                        //给经度、纬度、时间、地址赋值
//                        longitude = aMapLocation.getLongitude() + "";
//                        latitude = aMapLocation.getLatitude() + "";
                        MyApplication.times = df.format(date);
                        MyApplication.address = aMapLocation.getCity() + "," + aMapLocation.getDistrict() + "," + aMapLocation.getStreet() + "," + aMapLocation.getStreetNum();
                        Log.d("信息", aMapLocation.getLatitude() + ","
                                + aMapLocation.getLongitude() + ","
                                + aMapLocation.getProvince() + ","
                                + aMapLocation.getCity() + ","
                                + aMapLocation.getDistrict() + ","
                                + aMapLocation.getStreet() + ","
                                + aMapLocation.getStreetNum() + ","
                                + df.format(date)
                        );
                        NetWorkTools.AddRecordDetail_GarbageCollector(MyApplication.identificationCode, MyApplication.carId, MyApplication.driverId, "4", aMapLocation.getLatitude() + "",aMapLocation.getLongitude() + "",code.getText().toString(),fullScale.getText().toString(), new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d("返回的值", NetWorkTools.replaceBlank(result));
                                try {
                                    JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                                    JSONObject data = json.getJSONArray("data").getJSONObject(0);
                                    Toast.makeText(AddGarbageCollectorActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
//                                    Toast.makeText(AddGarbageCollectorActivity.this, e+"", Toast.LENGTH_SHORT).show();
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
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }
}
