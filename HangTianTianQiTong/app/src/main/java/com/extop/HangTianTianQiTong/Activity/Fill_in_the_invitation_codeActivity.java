package com.extop.HangTianTianQiTong.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;

/**
 * Created by Administrator on 2017/4/28.
 * 填写邀请码
 */

public class Fill_in_the_invitation_codeActivity extends ToolbarWebViewActivity {
    String url = MyApplication.url + "Fill_in_the_invitation_code.view?id=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置标题、设置返回按钮并添加监听事件
        tw_title.setText("新增我的下级");
        xWalkView.loadUrl(url + MyApplication.circleID);
        Log.d("ds", "onCreate: "+url + MyApplication.circleID);
    }
}
