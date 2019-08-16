package com.example.administrator.signaturetest.PullToRefreshAndLoad;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.administrator.signaturetest.R;

/**
 * Created by Administrator on 2017/7/20.
 * 下拉刷新和上拉加载更多
 */

public class PullToRefreshAndLoadActivity extends AppCompatActivity implements OnRefreshListener, OnLoadMoreListener {
    SwipeToLoadLayout swipeToLoadLayout;
    WebView webView;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置布局
        setContentView(R.layout.pull_to_refresh_and_load);
        //获得刷新布局控件
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);

        webView= (WebView) findViewById(R.id.swipe_target);
        //设置刷新监听
        swipeToLoadLayout.setOnRefreshListener(this);
        //设置加载更多监听
        swipeToLoadLayout.setOnLoadMoreListener(this);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等 :
        webView.setWebChromeClient(new WebChromeClient() {
            //处理alert弹出框
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                result.cancel();
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        //设置自适应屏幕，两者合用
        webView.getSettings().setUseWideViewPort(true);//将图片调整到适合webview的大小
        webView.getSettings().setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webView.loadUrl("http://www.baidu.com");
//        autoRefresh();自动刷新方法
        //给Handler赋值
        handler=new Handler();
    }
    //下拉刷新
    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
//                mAdapter.add("REFRESH:\n" + new Date());
                handler.post(new Runnable(){//用handler转交UI处理操作
                    @Override public void run(){
                        //在这写操作webview的代码
                        //请不要在这里放耗时代码，否则会卡住UI线程
                        webView.loadUrl("http://moe.hao123.com/");
                    }
                });
            }
        }, 2000);
    }
    //上拉加载更多
    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
                Toast.makeText(getApplicationContext(),"aa",Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }
    //自动刷新
    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }
}
