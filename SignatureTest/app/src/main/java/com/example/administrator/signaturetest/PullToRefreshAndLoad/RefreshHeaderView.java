package com.example.administrator.signaturetest.PullToRefreshAndLoad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/7/9.
 * 刷新头部
 */

public class RefreshHeaderView extends TextView implements SwipeRefreshTrigger, SwipeTrigger {

    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onRefresh() {
//        setText("REFRESHING");
        setText("正在刷新中……");
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled >= getHeight()) {
//                setText("RELEASE TO REFRESH");
                setText("松开刷新");
            } else {
//                setText("SWIPE TO REFRESH");
                setText("最后刷新时间: " + getLastUpdateTime());
            }
        } else {
//            setText("REFRESH RETURNING");
            setText("刷新成功");
        }
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
        setText("刷新成功");
    }

    @Override
    public void onReset() {
        setText("");
    }
    /**
     * 获得系统的最新时间
     *
     * @return
     */
    private String getLastUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }
}
