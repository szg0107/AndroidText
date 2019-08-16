package com.extop.HangTianTianQiTong.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.extop.HangTianTianQiTong.Adapter.SharedHelper;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;


import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.util.Map;

import static android.app.Activity.RESULT_OK;

//我的圈子
public class MyGroupFragment extends Fragment implements OnRefreshListener{
    private static final String ARG_PARAM1 = "param1";
    private String mParam1, inputContent;
    private XWalkView wv_MyGroup;
    String url = MyApplication.url + "circleList.view";
    private SearchView sw_searchbox;
    //读取本地文件内容
    private SharedHelper sharedHelper;
    //九宫格模式是否开启
    private boolean isJGG = true,circleSearch=false;
    //上拉加载控件和Handler监听
    SwipeToLoadLayout swipeToLoadLayout;
    Handler handler;
    Intent intent;

    public static MyGroupFragment newInstance(String param1) {
        MyGroupFragment fragment = new MyGroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment 获取布局
        View view = inflater.inflate(R.layout.fragment_my_group, container, false);
        wv_MyGroup = (XWalkView) view.findViewById(R.id.swipe_target);

        //上拉加载布局设置监听
        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        //下拉刷新监听
        swipeToLoadLayout.setOnRefreshListener(this);
        //上拉加载更多监听
//        swipeToLoadLayout.setOnLoadMoreListener(this);
        handler = new Handler();
        //搜索控件加载与设置属性
        sw_searchbox = (SearchView) view.findViewById(R.id.search_box);
        sw_searchbox.setIconifiedByDefault(false);//设置为false直接展开显示 左侧有放大镜 右侧无叉叉 有输入内容后有叉叉
        sw_searchbox.setSubmitButtonEnabled(true);//显示查询提交按钮
        sw_searchbox.setQueryHint("搜索圈子");//设置提示
        wv_MyGroup.setResourceClient(new XWalkResourceClient(wv_MyGroup){
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv_MyGroup.setUIClient(new XWalkUIClient(wv_MyGroup){
            //处理alert弹出框
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                result.cancel();
                return true;
            }
        });
        //设置自适应屏幕，两者合用
        wv_MyGroup.getSettings().setUseWideViewPort(true);//将图片调整到适合webView的大小
        wv_MyGroup.getSettings().setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        wv_MyGroup.loadUrl("javascript:document.body.contentEditable=true;");//设置WebView属性,运行执行js脚本
        wv_MyGroup.addJavascriptInterface(this, "callByJs");
        sw_searchbox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 单击搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                circleSearch=true;
//                wv_MyGroup.loadUrl("javascript:search('" + query.trim() + "');");
                intent = new Intent("android.intent.Activity.SearchCircleActivity");
                intent.putExtra("circleName", query.trim());
                //防止数据两次加载
                inputContent = query.trim();
                sw_searchbox.setIconified(true);
                sw_searchbox.clearFocus();
                //startActivityForResult()方法接收两个参数，第一个参数还是 Intent，第二个参数是请求码，用于在之后的回调中判断数据的来源。
                startActivityForResult(intent, 1);
                return false;
            }

            // 用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //超管控制是否开启搜索圈子功能
        if (MyApplication.isOpenSearch.equals("关闭")) {
            sw_searchbox.setVisibility(View.GONE);
        }
        readSharedHelper();
//        wv_MyGroup.loadUrl(url);//调用loadView方法为WebView加入链接
        webViewLoadURL();
        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        if (wv_MyGroup != null) {
            wv_MyGroup.pauseTimers();
            wv_MyGroup.onHide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wv_MyGroup != null) {
            wv_MyGroup.resumeTimers();
            wv_MyGroup.onShow();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wv_MyGroup != null) {
            wv_MyGroup.onDestroy();
        }
    }
    /*onActivityResult()方法带有三个参数，第一个参数 requestCode，即我们在启动活动时传入的请求码。
    第二个参数 resultCode，即我们在返回数据时传入的处理结果。
    第三个参数 data，即携带着返回数据的 Intent。
    由于在一个活动中有可能调用 startActivityForResult()方法去启动很多不同的活动，每一个活动返回的数据都会回调到 onActivityResult()这个方法中，因此我们首先要做的就是通过检查 requestCode 的值来判断数据来源。*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //废弃
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
//                    String returnedData = data.getStringExtra("circleid");
//                    wv_MyGroup.loadUrl("javascript:deleteAttention('" + returnedData + "')");
                    webViewLoadURL();
                }
                break;
        }
    }

    //得到圈子id、圈子名称、圈子管理员、搜索的圈子名
    @JavascriptInterface
    public void setValue(String circleID, String circleName, String circleAdmin, String circleName2) {
        MyApplication.circleID = circleID;
        if (circleName2.length() == 0) {
            MyApplication.circleName = circleName;
        } else {
            MyApplication.circleName = inputContent;
        }
        intent = new Intent("android.intent.Activity.CircleDetailsActivity");
        intent.putExtra("admin", circleAdmin);
        //startActivityForResult()方法接收两个参数，第一个参数还是 Intent，第二个参数是请求码，用于在之后的回调中判断数据的来源。
        startActivityForResult(intent, 1);
//        startActivity(intent);
    }

    //得到认证状态
    @JavascriptInterface
    public void certification(String circleRZ, String userRZ, String videoYZ,String circleIcon) {
        Log.d("circleRZ", circleRZ);
        Log.d("userRZ", userRZ);
        Log.d("videoYZ", videoYZ);
        Log.d("circleIcon",circleIcon);
        MyApplication.circleRZ = circleRZ;
        MyApplication.userRZ = userRZ;
        MyApplication.videoYZ = videoYZ;
        if(circleIcon!="undefined"){
            MyApplication.circleIcon=circleIcon;
        }
    }

    //上拉加载事件
    /*OnLoadMoreListener
    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
                wv_MyGroup.loadUrl("javascript:loadMore(" + 2 + ")");
            }
        }, 500);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        if (circleSearch){
            webViewLoadURL();
            circleSearch=false;
        }
    }

    //下拉刷新事件
    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
//                mAdapter.add("REFRESH:\n" + new Date());
                handler.post(new Runnable() {//用handler转交UI处理操作
                    @Override
                    public void run() {
                        //在这写操作webview的代码
                        //请不要在这里放耗时代码，否则会卡住UI线程
//                        wv_MyGroup.loadUrl(url);
                        webViewLoadURL();
                    }
                });
            }
        }, 500);
    }

    //读取本地文件中无图模式是否开启
    public void readSharedHelper() {
        sharedHelper = new SharedHelper(getContext());
        Map<String, Object> data = sharedHelper.read();
        if (data.get("isCircleJGG").toString().equals("true")) {
            isJGG = true;
        } else {
            isJGG = false;
        }
    }

    //webView加载页面
    public void webViewLoadURL() {
        if (isJGG) {
            wv_MyGroup.loadUrl(url + "?isJGG=" + isJGG);
            Log.d("圈列表",url + "?isJGG=" + isJGG);
        } else {
            wv_MyGroup.loadUrl(url);
            Log.d("圈列表",url);
        }
    }

}
