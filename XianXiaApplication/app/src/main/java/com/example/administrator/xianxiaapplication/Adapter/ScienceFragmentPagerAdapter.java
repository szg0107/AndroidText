package com.example.administrator.xianxiaapplication.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.xianxiaapplication.Fragment.ScienceContentFragment;

/**
 * Created by Administrator on 2016/8/1.
 */

public class ScienceFragmentPagerAdapter extends FragmentPagerAdapter {
    private String [] titlesen=new String[]{"hot","frontier","review","interview","visual","brief","fact","techb"};
    private String[] titles = new String[]{"热点", "前沿", "评论", "专访", "视觉","速读","谣言粉碎机","商业科技"};
    private Context context;
    public  ScienceFragmentPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }
    @Override
    public Fragment getItem(int position) {
        return ScienceContentFragment.newInstance(titlesen[position]);
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
