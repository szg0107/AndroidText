package com.extop.HangTianTianQiTong.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;

/**
 * Created by Administrator on 2017/7/19.
 * 圈内缴费记录
 */

public class InternalPaymentRecordsActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url+ "payment_records_incircle.view?id=";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tw_title.setText(intent.getStringExtra("title"));
        //获取当前时间戳
//        Date d = new Date();
//        long timestamp=d.getTime();
        Log.d("圈内缴费记录",url+MyApplication.circleID);
        xWalkView.loadUrl(url+MyApplication.circleID);//调用loadView方法为WebView加入链接
    }
}
