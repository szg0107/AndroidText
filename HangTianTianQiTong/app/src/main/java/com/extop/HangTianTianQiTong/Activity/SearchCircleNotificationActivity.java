package com.extop.HangTianTianQiTong.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * Created by Administrator on 2017/9/15.
 * 搜索圈子通知
 */

public class SearchCircleNotificationActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "circle_notification.view?id=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("搜索结果");

        xWalkView.setUIClient(new XWalkUIClient(xWalkView){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                dialog(message);
                result.cancel();
                return true;
            }
        });


        xWalkView.loadUrl(url + MyApplication.circleID + "&type=inform&reuse=" + intent.getStringExtra("reuse") + "&content=" + intent.getStringExtra("content"));//调用loadView方法为WebView加入链接
        Log.d("url", url + MyApplication.circleID + "&type=inform&reuse=" + intent.getStringExtra("reuse") + "&content=" + intent.getStringExtra("content"));
    }

    //详情弹窗
    protected void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchCircleNotificationActivity.this, R.style.AlertDialog);
        builder.setMessage(message);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    @JavascriptInterface
    public void setValue(String string) {
        intent = new Intent("android.intent.Activity.InformTheDetailsActivity");
        intent.putExtra("id", string);
        startActivity(intent);
    }
}
