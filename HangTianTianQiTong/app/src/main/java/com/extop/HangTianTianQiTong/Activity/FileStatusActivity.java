package com.extop.HangTianTianQiTong.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;

import org.xwalk.core.JavascriptInterface;

/**
 * Created by szg on 2018/3/6.
 * 文件状态
 */

public class FileStatusActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "fileStatus.view?taskId=";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("文件状态");
        //获取当前时间戳
//        Date d = new Date();
//        long timestamp=d.getTime();
//        Log.d("时间戳",timestamp+"");
        xWalkView.loadUrl(url + intent.getStringExtra("taskID")+"&taskFounder="+intent.getStringExtra("taskFounder"));//调用loadView方法为WebView加入链接
        Log.d("url", url + intent.getStringExtra("taskID")+"&taskFounder="+intent.getStringExtra("taskFounder"));
    }

    @JavascriptInterface
    public void setValue(String string) {
        finish();
    }
}
