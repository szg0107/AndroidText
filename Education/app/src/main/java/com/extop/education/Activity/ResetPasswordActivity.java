package com.extop.education.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;

import org.xwalk.core.JavascriptInterface;

/**
 * Created by Administrator on 2017/4/11.
 * 重置密码
 */

public class ResetPasswordActivity extends ToolbarWebViewActivity {

    String url = MyApplication.url+"Resetpassword.view?phoneNumber=";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        tw_title.setText("重置密码");
        xWalkView.loadUrl(url+intent.getStringExtra("phoneNumber"));//调用loadView方法为WebView加入链接
    }

    @JavascriptInterface
    public void setValue(String string) {
        Log.d("重置成功",string);
        intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
