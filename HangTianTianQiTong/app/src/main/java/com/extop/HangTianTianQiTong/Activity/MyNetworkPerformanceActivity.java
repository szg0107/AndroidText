package com.extop.HangTianTianQiTong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

/**
 * Created by Administrator on 2017/4/28.
 * 我的网络业绩
 */

public class MyNetworkPerformanceActivity extends ToolbarWebViewActivity {
    String url = MyApplication.url + "MyNetworkPerformance.view?id=";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置标题、设置返回按钮并添加监听事件
        tw_title.setText(intent.getStringExtra("title"));

        toolbar.inflateMenu(R.menu.my_questions_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                intent = new Intent("android.intent.Activity.Fill_in_the_invitation_codeActivity");
                startActivity(intent);
                return false;
            }
        });
        xWalkView.loadUrl(url+ MyApplication.circleID);
    }
}
