package com.szg.homemakingapplication.support.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.szg.homemakingapplication.support.adapter.fragment.TlayIndentNavigationContentFragment;
import com.szg.homemakingapplication.support.adapter.fragment.TlayIndentViewPagercontentFragment;

/**
 * Created by Administrator on 2016/8/22.
 */

public class TlayIndentNavigationFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"已抢单订单", "待服务订单", "代付款订单", "已完成订单"};
    private String[] state = new String[]{"1","2","5","6"};
    private Context context;

    public TlayIndentNavigationFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return TlayIndentViewPagercontentFragment.newInstance(state[position]);
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
