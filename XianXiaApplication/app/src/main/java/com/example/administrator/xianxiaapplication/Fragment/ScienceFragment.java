package com.example.administrator.xianxiaapplication.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.administrator.xianxiaapplication.Adapter.ScienceFragmentPagerAdapter;
import com.example.administrator.xianxiaapplication.R;

import java.io.Serializable;

public class ScienceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private  FragmentManager fg;

    public static ScienceFragment newInstance(String param1) {
        ScienceFragment fragment = new ScienceFragment();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_science, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        AppCompatActivity ACA = (AppCompatActivity) getActivity();
        FragmentManager FM= getChildFragmentManager();
        ScienceFragmentPagerAdapter adapter = new ScienceFragmentPagerAdapter(FM,
                ACA);
        viewPager.setAdapter(adapter);

        //TabLayout
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        return view;
    }
}
