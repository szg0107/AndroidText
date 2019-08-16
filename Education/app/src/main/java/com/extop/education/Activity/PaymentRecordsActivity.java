package com.extop.education.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;
import java.util.Date;


/**
 * Created by Administrator on 2017/6/23.
 * 缴费记录
 */

public class PaymentRecordsActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url+ "paymentRecords.view?timestamp=";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("缴费记录");
        //获取当前时间戳
        Date d = new Date();
        long timestamp = d.getTime();
        Log.d("缴费记录", url + timestamp + "");
        xWalkView.loadUrl(url + timestamp);//调用loadView方法为WebView加入链接
    }
}
