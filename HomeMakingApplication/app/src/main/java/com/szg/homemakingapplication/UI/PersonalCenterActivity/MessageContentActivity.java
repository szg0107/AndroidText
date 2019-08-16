package com.szg.homemakingapplication.UI.PersonalCenterActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.szg.homemakingapplication.MyApplication;
import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.accesstool.NetWorkTools;
import com.szg.homemakingapplication.model.NoticeBean;

/**
 * Created by Administrator on 2016/9/12.
 */

public class MessageContentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.message_list_content);
        Toolbar tbgoback= (Toolbar) findViewById(R.id.toolbar_message_content);
        tbgoback.setNavigationIcon(R.drawable.arrow_left_d);
        tbgoback.setTitle("通知详情");
        //点导航栏图标退出
        tbgoback.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        WebView wv= (WebView) findViewById(R.id.message_wv);
        Intent intent=getIntent();
        NoticeBean noticeBean=new NoticeBean();
        noticeBean= (NoticeBean) intent.getSerializableExtra("NoticeBean");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient());

        wv.loadUrl(NetWorkTools.url+ "/notice/getNoticeInfo" +
                "?noticeid=" + noticeBean.getNoticeid()+"&token="+ (MyApplication.userBean.getToken()));
    }
}
