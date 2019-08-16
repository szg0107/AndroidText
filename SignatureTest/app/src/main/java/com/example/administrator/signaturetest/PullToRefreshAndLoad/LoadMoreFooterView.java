package com.example.administrator.signaturetest.PullToRefreshAndLoad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by Administrator on 2017/7/9.
 * 加载更多尾部
 */

public class LoadMoreFooterView extends TextView implements SwipeTrigger, SwipeLoadMoreTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onLoadMore() {
//        setText("LOADING MORE");
        setText("加载更多……");
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (yScrolled <= -getHeight()) {
//                setText("RELEASE TO LOAD MORE");
                setText("松开加载更多");
            } else {
//                setText("SWIPE TO LOAD MORE");
            }
        } else {
//            setText("LOAD MORE RETURNING");
        }
    }

    @Override
    public void onRelease() {
//        setText("LOADING MORE");
        setText("加载更多……");
    }

    @Override
    public void onComplete() {
//        setText("COMPLETE");
    }

    @Override
    public void onReset() {
        setText("");
    }
}
