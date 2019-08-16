package com.example.administrator.signaturetest.NativePositioning;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.signaturetest.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;


/**
 * Created by Administrator on 2018/1/22.
 *高德web服务+定位+反向地理编码
 */

public class ShowLocation extends AppCompatActivity {
    private String provider,latitude,longitude,url,sig;//位置提供器
    private LocationManager locationManager;//位置服务
    private Location location;
    private Button btn_show;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        init();//关联控件
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//获得位置服务
        provider =NetWorkTools.judgeProvider(locationManager);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }
    private void init() {
        btn_show = (Button) findViewById(R.id.btn);
        tv_show = (TextView) findViewById(R.id.textView);
    }

    private void test(){
        if (provider != null) {//有位置提供器的情况
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                latitude = location.getLatitude()+"";
                longitude = location.getLongitude()+"";
                sig=Md5Utils.md5("key=7e85aeb1a9a216ae44058a333b792e9d&location="+longitude+","+latitude+"b6da557ca7baf5ab90a23476a2d10493");
                url="http://restapi.amap.com/v3/geocode/regeo?location="+longitude+","+latitude+"&key=7e85aeb1a9a216ae44058a333b792e9d&sig="+sig;
            } else {
                Toast.makeText(this,"暂时无法获得当前位置",Toast.LENGTH_SHORT);
            }
        }else{//不存在位置提供器的情况

        }

        NetWorkTools.requestLog(url, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("请求数据",result);
                    JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                    JSONObject addressComponent=new JSONObject(regeocode.getString("addressComponent"));
                    String city = addressComponent.getString("province");
                    String district = addressComponent.getString("district");
                    String township = addressComponent.getString("township");
                    Log.d("district",city+district+township+"");
                    tv_show.setText(city+district+township);
                } catch (JSONException e) {
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
        Log.d("点击按钮","纬度："+latitude+"————————经度："+longitude);
    }


}
