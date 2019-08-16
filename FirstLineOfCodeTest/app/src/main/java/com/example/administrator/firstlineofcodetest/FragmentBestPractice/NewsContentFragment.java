package com.example.administrator.firstlineofcodetest.FragmentBestPractice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.firstlineofcodetest.R;

/**
 * Created by Administrator on 2018/3/18.
 */

public class NewsContentFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载news_content_frag布局
        view = inflater.inflate(R.layout.news_content_frag, container, false);
        return view;
    }
    //用于将新闻的标题和内容显示在界面上
    public void refresh(String newsTitle, String newsContent) {
        /*View.VISIBLE 常量值为0，意思是可见的
        *View.INVISIBLE 常量值为4，意思是不可见的
        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
        * */
        //获得标题和内容的外部控件使其显示
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        //获取新闻标题控件
        TextView newsTitleText = (TextView) view.findViewById(R.id.news_title);
        //获取新闻内容控件
        TextView newsContentText= (TextView) view.findViewById(R.id.news_content);
        //刷新新闻的标题
        newsTitleText.setText(newsTitle);
        //刷新新闻的内容
        newsContentText.setText(newsContent);
    }
}
