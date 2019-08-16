package com.extop.HangTianTianQiTong.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkNavigationHistory;
import java.util.Date;


/**
 * Created by Administrator on 2017/7/25.
 * 我的应用
 */

public class MyController extends ToolbarWebViewActivity {
    private String url = "", circleAdmin = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText(intent.getStringExtra("title"));
        url = intent.getStringExtra("url");
        Log.d("应用url", url);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xWalkView.getNavigationHistory().canGoBack()) {
                    xWalkView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);//返回上一页面
                } else {
                    finish();
                }
            }
        });
        //获取当前时间戳
        Date d = new Date();
        long timestamp = d.getTime();
        Log.d("时间戳", timestamp + "");
        xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
        Log.d("MyControllerurl", url);
        toolbar.inflateMenu(R.menu.close_activity);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });
    }

    //得到圈子id、圈子名称、圈子管理员、搜索的圈子名
    @JavascriptInterface
    public void setValue(String circleID, String circleName, String circleAdmin, String circleName2) {
        MyApplication.circleID = circleID;
        this.circleAdmin = circleAdmin;
        if (circleName2.length() == 0) {
            MyApplication.circleName = circleName;
        }
        intent = new Intent("android.intent.Activity.CircleDetailsActivity");
        intent.putExtra("admin", circleAdmin);
        //startActivityForResult()方法接收两个参数，第一个参数还是 Intent，第二个参数是请求码，用于在之后的回调中判断数据的来源。
        startActivityForResult(intent, 1);
        //关闭圈子详情页面
        MyApplication.finishSingleActivityByClass(CircleDetailsActivity.class);
        //关闭自身页面
        finish();
    }


    //得到认证状态
    @JavascriptInterface
    public void certification(String circleRZ, String userRZ, String videoYZ,String circleIcon) {
        Log.d("circleRZ", circleRZ);
        Log.d("userRZ", userRZ);
        Log.d("videoYZ", videoYZ);
        Log.d("circleIcon",circleIcon);
        MyApplication.circleRZ = circleRZ;
        MyApplication.userRZ = userRZ;
        MyApplication.videoYZ = videoYZ;
    }

    @JavascriptInterface
    public void getCircleName(String circleName) {
        MyApplication.circleName = circleName;
        dialog();
    }

    //加入提示
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyController.this, R.style.AlertDialog);
        builder.setMessage("确认要加入该圈子吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //webView在另外的一个线程中使用loadUrl 调用搜索圈子方法
                xWalkView.post(new Runnable() {
                    @Override
                    public void run() {
                        xWalkView.loadUrl("javascript:search('" + MyApplication.circleName.trim() + "')");
                    }
                });
                //关闭弹窗
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                xWalkView.post(new Runnable() {
                    @Override
                    public void run() {
//                        xWalkView.getNavigationHistory().canGoBack();
                        xWalkView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);//返回上一页
                    }
                });

                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
}
