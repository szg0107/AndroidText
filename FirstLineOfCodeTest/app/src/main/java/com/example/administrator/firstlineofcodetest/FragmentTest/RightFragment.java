package com.example.administrator.firstlineofcodetest.FragmentTest;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.firstlineofcodetest.R;

public class RightFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //当碎片和活动建立关联的时候调用
        Log.d("RightFragment", "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //为碎片创建视图（加载布局）时调用
        Log.d("RightFragment", "onCreateView: ");
        View view=inflater.inflate(R.layout.right_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //确保与碎片相关联的活动一定已经创建完毕的时候调用
        Log.d("RightFragment", "onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("RightFragment", "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("RightFragment", "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("RightFragment", "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("RightFragment", "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //当与碎片关联的视图被移除的时候调用
        Log.d("RightFragment", "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("RightFragment", "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //当碎片和活动解除关联的时候调用
        Log.d("RightFragment", "onDetach: ");
    }
}
