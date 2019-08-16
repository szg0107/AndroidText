package com.extop.HangTianTianQiTong.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;


/**
 * Created by Administrator on 2017/5/2.
 * 我的综合评价
 */

public class MyComprehensiveEvaluationActivity extends ToolbarWebViewActivity {
    String url = MyApplication.url + "MyComprehensiveEvaluation.view?id=";
    /*MyComprehensiveEvaluation.view test_page*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置标题、设置返回按钮并添加监听事件
        tw_title.setText(intent.getStringExtra("title"));
        xWalkView.loadUrl(url + MyApplication.circleID);
        Log.d("地址",url + MyApplication.circleID );
    }
}
