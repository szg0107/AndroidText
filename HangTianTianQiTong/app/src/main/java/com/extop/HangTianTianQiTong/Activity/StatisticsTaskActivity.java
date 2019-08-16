package com.extop.HangTianTianQiTong.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.extop.HangTianTianQiTong.Adapter.NetWorkTools;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * Created by Administrator on 2017/8/30.
 * 统计类任务
 */

public class StatisticsTaskActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "statistics_task.view?taskId=";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        tw_title.setText("任务详情");
        //获取当前时间戳
//        Date d = new Date();
//        long timestamp=d.getTime();
//        Log.d("时间戳",timestamp+"");
        xWalkView.setUIClient(new XWalkUIClient(xWalkView){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                NetWorkTools.dialog(StatisticsTaskActivity.this, message);
                result.cancel();
                return true;
            }
        });
        xWalkView.loadUrl(url + intent.getStringExtra("taskID"));//调用loadView方法为WebView加入链接
        Log.d("url", url + intent.getStringExtra("taskID"));
    }

    @JavascriptInterface
    public void setValue(String string) {
        finish();
    }
}
