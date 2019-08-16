package com.extop.education.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkNavigationHistory;

/**
 * Created by Administrator on 2017/9/17.
 * 完成任务详情
 */

public class CompleteTaskDetailsActivity extends ToolbarWebViewActivity {
    private String url = "", taskType = "", taskID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("任务详情");
        taskType = intent.getStringExtra("taskType");
        taskID = intent.getStringExtra("taskID");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xWalkView.getNavigationHistory().canGoBack()) {
                    xWalkView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
                } else {
                    finish();
                }
            }
        });
        switch (taskType){
            case "15":
                //选择类
                url= MyApplication.url+"ywc_xzrw.view?taskId="+taskID;
                xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
                break;
            case "16":
                //缴费类
                url= MyApplication.url+"ywc_jfrw.view?taskId="+taskID;
                xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
                break;
            case "17":
                //图文类
                url= MyApplication.url+"ywc_twrw.view?taskId="+taskID;
                xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
                break;
            case "23":
                //考试类
                url= MyApplication.url+"ywc_ksrw.view?taskId="+taskID;
                xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
                break;
            case "24":
                //统计类
                url= MyApplication.url+"ywc_tjrw.view?taskId="+taskID;
                xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
                break;
            case "25":
                //问卷类
                url= MyApplication.url+"ywc_wjrw.view?taskId="+taskID;
                xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
                break;
            case "26":
                //审核类
                url= MyApplication.url+"ywc_shrw.view?taskId="+taskID;
                xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
                break;
            case "27":
                //差异通知类
                url= MyApplication.url+"cytzlrw.view?taskId="+taskID+"&ywc=true";
                xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
                break;
        }
        Log.d("MyControllerurl", url);
    }

    @JavascriptInterface
    public void setValue(String[] string) {
    }
}
