package com.extop.HangTianTianQiTong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import org.xwalk.core.JavascriptInterface;

/**
 * Created by Administrator on 2016/12/7.
 * 注册绑定手机号
 */

public class MessageAuthenticationActivity extends ToolbarWebViewActivity {
    String url = MyApplication.url + "phoneRegistered.view";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        tw_title.setText("注册");
        xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
    }

    @JavascriptInterface
    public void setValue(String string) {
        if (string.length() != 0) {
            intent = new Intent("android.intent.Activity.SetPasswordActivity");
            intent.putExtra("phoneNumber", string);
            startActivity(intent);
            finish();
        }
    }
}
