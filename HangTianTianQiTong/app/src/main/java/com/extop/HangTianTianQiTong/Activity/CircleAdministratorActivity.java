package com.extop.HangTianTianQiTong.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * Created by Administrator on 2017/3/23.
 * 申请圈子管理员
 */

public class CircleAdministratorActivity extends ToolbarWebViewActivity {
    String url =MyApplication.url+ "CircleAdministrator.view?circleID=";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        tw_title.setText(intent.getStringExtra("title"));
        xWalkView.setUIClient(new XWalkUIClient(xWalkView){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                Toast toast=Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
                //设置Toast位置
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                //设置Toast字体大小
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                v.setTextSize(20);
                toast.show();
                result.cancel();
                return true;
            }
        });
        xWalkView.loadUrl(url+MyApplication.circleID+"&circleName="+MyApplication.circleName);//调用loadView方法为WebView加入链接
    }

    //js可以调用的方法
    @JavascriptInterface
    public void setValue(String string) {
        if (string.equals("hello android!!")){
//            Toast.makeText(getApplicationContext(),"提交成功",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
