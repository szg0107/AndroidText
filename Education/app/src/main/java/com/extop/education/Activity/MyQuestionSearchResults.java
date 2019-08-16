package com.extop.education.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;
import com.extop.education.R;

import org.xwalk.core.JavascriptInterface;


/**
 * Created by Administrator on 2017/4/24.
 * 我的提问搜索结果
 */

public class MyQuestionSearchResults extends ToolbarWebViewActivity {
    private String url = MyApplication.url+"searchMyQuestion.view?id=",reuse="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        tw_title.setText("搜索结果");
        intent=getIntent();
        reuse=intent.getStringExtra("reuse");
        xWalkView.loadUrl(url+ MyApplication.circleID+"&content="+intent.getStringExtra("content")+"&reuse="+reuse);//调用loadView方法为WebView加入链接
        Log.d("搜索我的提问", url+ MyApplication.circleID+"&content="+intent.getStringExtra("content")+"&reuse="+reuse);
    }
    @JavascriptInterface
    public void setValue(String string) {
        intent=new Intent("android.intent.Activity.QuestionDetailsActivity");
        intent.putExtra("id",string);
        intent.putExtra("reuse",reuse);
        startActivity(intent);
    }
    @JavascriptInterface
    public void isVIP(){
        dialogs();
    }

    protected void  dialogs(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialog);
        builder.setMessage("您不是本圈子会员！请进行充值！");
        builder.setPositiveButton("充值", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent("android.intent.Activity.WeChat_Alipay_pay");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
}
