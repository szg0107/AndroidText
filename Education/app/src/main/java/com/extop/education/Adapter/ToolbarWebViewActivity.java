package com.extop.education.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;
import com.extop.education.R;

import org.xwalk.core.XWalkView;

import static com.extop.education.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/10/8.
 * Toolbar+WebView公共类
 */

public class ToolbarWebViewActivity extends AppCompatActivity {
    protected XWalkView xWalkView;
    protected Intent intent;
    protected TextView tw_title;//标题
    protected Toolbar toolbar;//标题栏控件
    protected SearchView tw_search_box;//搜索控件
    protected ScaleImageView scaleImageView;//加载图片控件
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        NetWorkTools.taskbar_transparent(window, this);

        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.toolbar_webview);

        //键盘弹起窗口可拉伸
        assistActivity(this);

        //获取WebView并设置属性
        xWalkView = (XWalkView) findViewById(R.id.tw_XWalkView);
        NetWorkTools.XWalkView_Settings(xWalkView,this);
        //获取控件
        tw_title = (TextView) findViewById(R.id.tw_title);
        //设置标题
        intent = getIntent();
        tw_title.setText(intent.getStringExtra("title"));

        toolbar = (Toolbar) findViewById(R.id.tw_icon);
        /*设置菜单并监听单击事件
        tb_detailsIcon.inflateMenu(R.menu.my_questions_menu);
        tb_detailsIcon.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                intent = new Intent("android.intent.Activity.Fill_in_the_invitation_codeActivity");
                startActivity(intent);
                return false;
            }
        });*/
        //设置返回按钮并添加监听事件
        toolbar.setNavigationIcon(R.mipmap.arrow_left_d);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //获取搜索控件
        tw_search_box= (SearchView) findViewById(R.id.tw_search_box);

        //获取背景图片
        scaleImageView= (ScaleImageView) findViewById(R.id.tw_image);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (xWalkView != null) {
            xWalkView.pauseTimers();
            xWalkView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (xWalkView != null) {
            xWalkView.resumeTimers();
            xWalkView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (xWalkView != null) {
            xWalkView.onDestroy();
        }
    }
}
