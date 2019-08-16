package com.extop.HangTianTianQiTong.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;

/**
 * Created by Administrator on 2017/11/2.
 * 积分排名
 */

public class LeaderBoardActivity extends ToolbarWebViewActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xWalkView.loadUrl(MyApplication.url+"leaderBoard.view?id="+MyApplication.circleID);
    }
}
