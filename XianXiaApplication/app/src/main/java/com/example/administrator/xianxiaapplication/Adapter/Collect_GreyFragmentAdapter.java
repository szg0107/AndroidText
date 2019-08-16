package com.example.administrator.xianxiaapplication.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.xianxiaapplication.Fragment.Collect_GreyContentFragment;

/**
 * Created by Administrator on 2016/7/27.
 */

public class Collect_GreyFragmentAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"日报", "阅读", "新闻", "科学"};
    private Context context;
    public  Collect_GreyFragmentAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }
    @Override
    public Fragment getItem(int position) {
        return Collect_GreyContentFragment.newInstance(titles[position]);
    }

    @Override
    public int getCount() {
        return titles.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
