package com.extop.education.Activity;

import android.content.Intent;
import android.os.Bundle;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;

import org.xwalk.core.JavascriptInterface;

/**
 * Created by Administrator on 2016/12/7.
 * 修改密码
 */

public class ChangePasswordActivity extends ToolbarWebViewActivity {
    String url = MyApplication.url+"ChangePassword.view";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.addActivity(this);
        //获取控件
        tw_title.setText("修改密码");
        xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
    }

    //js可以调用的方法
    @JavascriptInterface
    public void setValue(String string) {
        if (string.equals("hello android!!")){
            intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            MyApplication.finishActivity();
            finish();
        }
    }
}
