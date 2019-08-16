package com.example.administrator.xianxiaapplication.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.administrator.xianxiaapplication.Fragment.ReadingContentFragment;

/**
 * Created by Administrator on 2016/7/27.
 */

public class ReadingFragmentAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"综合", "文学", "程序员", "流行", "文化","生活","金融"};
    private Context context;
    public  ReadingFragmentAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }
    @Override
    public Fragment getItem(int position) {
        return ReadingContentFragment.newInstance(titles[position]);
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
