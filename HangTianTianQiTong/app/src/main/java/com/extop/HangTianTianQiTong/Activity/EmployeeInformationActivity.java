package com.extop.HangTianTianQiTong.Activity;



import android.os.Bundle;
import android.support.annotation.Nullable;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;

import org.xwalk.core.JavascriptInterface;

import io.rong.imkit.RongIM;


/**
 * Created by Administrator on 2017/7/31.
 * 员工信息
 */

public class EmployeeInformationActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url+ "staff_info.view?phoneNumber=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
//        tv_examination_title.setText(intent.getStringExtra("title"));

        xWalkView.loadUrl(url+intent.getStringExtra("phoneNumber"));//调用loadView方法为WebView加入链接
//        RongIM.getInstance().startPrivateChat(this,"1854891460", "许渺强");
    }

    @JavascriptInterface
    public void setValue(String phoneNumber,String name) {
//        intent = new Intent("android.intent.Activity.InteractiveChatActivity");
//        intent.putExtra("phoneNumber", phoneNumber);
//        intent.putExtra("name",name);
//        startActivity(intent);
        RongIM.getInstance().startPrivateChat(this,phoneNumber, name);
    }
}
