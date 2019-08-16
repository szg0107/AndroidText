package com.extop.HangTianTianQiTong.Activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;

/**
 * Created by Administrator on 2017/4/1.
 * 回复详情
 */

public class ReplyForDetailsActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "replyForDetails.view?replyID=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        tw_title.setText("回复详情");
        xWalkView.loadUrl(url + intent.getStringExtra("replyID") + "&QuestionID=" + intent.getStringExtra("QuestionID"));
    }

    @JavascriptInterface
    public void setValue(String string) {
        Log.d("1",string);
        if (string.equals("deleteQuestion")) {
            dialogs();
        }
    }

    protected void dialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReplyForDetailsActivity.this, R.style.AlertDialog);
        builder.setMessage("您确定要删除本条信息吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //webView在另外的一个线程中使用loadUrl
                xWalkView.post(new Runnable() {
                    @Override
                    public void run() {
                        xWalkView.loadUrl("javascript:delete_Answer('" + intent.getStringExtra("replyID") + "')");
                    }
                });

                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
}
