package com.extop.HangTianTianQiTong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.extop.HangTianTianQiTong.Adapter.NetWorkTools;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import static com.extop.HangTianTianQiTong.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/8/1.
 * 互动聊天
 */

public class InteractiveChatActivity extends AppCompatActivity {
    private Toolbar tb_icon;
    private String url = MyApplication.url+ "jstx.view?phoneNumber=";
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改状态栏颜色
        Window window=getWindow();
        NetWorkTools.taskbar_transparent(window,this);

        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //绑定布局
        setContentView(R.layout.conversation);
        //键盘弹起窗口可拉伸
        assistActivity(this);
        //获取控件
        tb_icon= (Toolbar) findViewById(R.id.tw_icon);
        TextView tv_examination_title= (TextView) findViewById(R.id.tw_title);
        intent=getIntent();
//        tv_examination_title.setText(intent.getStringExtra("name"));
        tv_examination_title.setText(intent.getData().getQueryParameter("title"));
        tb_icon.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
