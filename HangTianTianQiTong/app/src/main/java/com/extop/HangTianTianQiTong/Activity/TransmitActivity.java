package com.extop.HangTianTianQiTong.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import org.xwalk.core.JavascriptInterface;

/**
 * Created by szg on 2018/3/9.
 * 汇签任务传递页面
 */

public class TransmitActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url+"hqrwzzxz.view?taskId=";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("");
        //获取当前时间戳
//        Date d = new Date();
//        long timestamp=d.getTime();
//        Log.d("时间戳",timestamp+"");
//        webView.addJavascriptInterface(this,"callByJs");
        xWalkView.loadUrl(url+intent.getStringExtra("taskID")+"&cdfs="+intent.getStringExtra("transmittalModeValue")+"&circleId="+MyApplication.circleID);//调用loadView方法为WebView加入链接
        Log.d("url",url+intent.getStringExtra("taskID")+"&cdfs="+intent.getStringExtra("transmittalModeValue")+"&circleId="+MyApplication.circleID);
    }
    @JavascriptInterface
    public void setValue(String string) {
        MyApplication.finishSingleActivityByClass(CountersignTaskActivity.class);
        finish();
    }
}
