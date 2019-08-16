package com.example.administrator.xianxiaapplication.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.administrator.xianxiaapplication.Adapter.ReadingFragmentAdapter;
import com.example.administrator.xianxiaapplication.R;

public class ReadingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public static ReadingFragment newInstance(String param1) {
        ReadingFragment fragment = new ReadingFragment();
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
        View view=inflater.inflate(R.layout.fragment_reading, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerReading);
        AppCompatActivity ACA = (AppCompatActivity) getActivity();
        FragmentManager FM= ACA.getSupportFragmentManager();
        ReadingFragmentAdapter adapter = new ReadingFragmentAdapter(FM,
                ACA);
        viewPager.setAdapter(adapter);

        //TabLayout
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayoutReading);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        return view;
    }
}
