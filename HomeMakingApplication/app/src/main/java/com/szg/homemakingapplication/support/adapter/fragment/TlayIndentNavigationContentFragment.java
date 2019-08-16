package com.szg.homemakingapplication.support.adapter.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.support.adapter.TlayIndentNavigationFragmentPagerAdapter;

import org.xutils.x;

public class TlayIndentNavigationContentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    public static TlayIndentNavigationContentFragment newInstance(String param1) {
        TlayIndentNavigationContentFragment fragment = new TlayIndentNavigationContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_indent_navigation_content, container, false);
        TabLayout tabLayoutindent= (TabLayout) view.findViewById(R.id.tlayindentnavigation);
        ViewPager viewPager= (ViewPager) view.findViewById(R.id.vpindentcontent);
        TlayIndentNavigationFragmentPagerAdapter tli=new TlayIndentNavigationFragmentPagerAdapter(getChildFragmentManager(), x.app());
        viewPager.setAdapter(tli);
        tabLayoutindent.setupWithViewPager(viewPager);
        tabLayoutindent.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayoutindent.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }
}
