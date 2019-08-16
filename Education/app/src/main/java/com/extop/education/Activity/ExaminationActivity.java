package com.extop.education.Activity;

import android.content.DialogInterface;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;
import com.extop.education.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;


/**
 * Created by Administrator on 2017/4/18.
 * 我要考试
 */

public class ExaminationActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url+ "zyxz.view?id=";
    /*examination*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        tw_title.setText(intent.getStringExtra("title"));

        xWalkView.setUIClient(new XWalkUIClient(xWalkView){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                NetWorkTools.dialog(ExaminationActivity.this,message);
                result.cancel();
                return true;
            }
        });
        xWalkView.loadUrl(url+MyApplication.circleID);//调用loadView方法为WebView加入链接
    }

    protected void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExaminationActivity.this,R.style.AlertDialog);
        builder.setMessage(message);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
    //显示上一次考试成绩
    @JavascriptInterface
    public void setValue(String zongfen,String zhengquelv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExaminationActivity.this,R.style.AlertDialog);
        builder.setMessage("上次考试正确率："+zhengquelv+"%,成绩："+zongfen+"分。");
        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //解决webView调用JS出错
                xWalkView.post(new Runnable() {
                    @Override
                    public void run() {
                        xWalkView.loadUrl("javascript:start()");
                    }
                });
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
    //考试完成时显示
    @JavascriptInterface
    public void  testFinished(String message){
        dialog(message);
    }
}
