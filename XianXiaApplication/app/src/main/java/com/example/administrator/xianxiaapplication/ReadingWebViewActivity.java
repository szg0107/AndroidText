package com.example.administrator.xianxiaapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.administrator.xianxiaapplication.Instance.ArticleBean;
import com.example.administrator.xianxiaapplication.Instance.BookBean;

/**
 * Created by Administrator on 2016/8/7.
 */

public class ReadingWebViewActivity extends AppCompatActivity {
    private WebView webView;
    private long exitTime = 0;
    private Toolbar tb;
    private BookBean bookBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webviewcontent);
        Intent intent = getIntent();
        bookBean = (BookBean) intent.getSerializableExtra("book");
        tb = (Toolbar) findViewById(R.id.webToolbar);
        tb.setNavigationIcon(R.drawable.arrow_left_d);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        webView = (WebView) findViewById(R.id.webviewtext);
        webView.setWebViewClient(new WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        webView.loadUrl(bookBean.getEbook_url());
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                super.onBackPressed();
            }
        }

    }
}
