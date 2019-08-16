package com.example.administrator.xianxiaapplication.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.administrator.xianxiaapplication.Fragment.NewsContentFragment;

public class NewsFragmentAdapter extends FragmentPagerAdapter {
    private String[] page=new String[]{"news_province.xml","news_world.xml","news_mil.xml","news_photo.xml","news_ent.xml","news_calligraphy.xml","news_tech.xml","news_overseas.xml","news_finance.xml","news_fortune.xml","news_politics.xml","news_auto.xml","news_legal.xml","news_edu.xml"};
    private String[] titles = new String[]{"国内", "国际", "军事", "图片", "娱乐","艺术","科技","华人","金融","财经","时政","汽车","法制","教育"};
    private Context context;
    public  NewsFragmentAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }
    @Override
    public Fragment getItem(int position) {
        return NewsContentFragment.newInstance(page[position]);
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
