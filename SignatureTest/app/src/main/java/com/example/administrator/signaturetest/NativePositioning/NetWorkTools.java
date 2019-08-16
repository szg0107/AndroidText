package com.example.administrator.signaturetest.NativePositioning;


import android.location.LocationManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */

public class NetWorkTools {
    //反向地理编码
    public static void requestLog(String url, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, callback);
    }
    /**
     * 判断是否有可用的内容提供器
     * @return 不存在返回null
     */
    public static String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;
        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        }else{
//            Toast.makeText(ShowLocation.this,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
