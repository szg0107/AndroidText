package com.extop.education.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;
import org.xwalk.core.JavascriptInterface;

/**
 * Created by Administrator on 2017/4/11.
 * 找回密码
 */

public class RetrievePasswordActivity extends ToolbarWebViewActivity {
    String url = MyApplication.url + "Retrievepassword.view";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("找回密码");
        xWalkView.loadUrl(url);//调用loadView方法为WebView加入链接
    }

    @JavascriptInterface
    public void setValue(String string) {
        if (string.length() != 0) {
            intent = new Intent("android.intent.Activity.ResetPasswordActivity");
            intent.putExtra("phoneNumber", string);
            startActivity(intent);
            finish();
        }
    }
}
