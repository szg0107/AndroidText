package com.szg.homemakingapplication.support.adapter.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.accesstool.NetWorkTools;
import com.szg.homemakingapplication.model.OrderBean;
import com.szg.homemakingapplication.model.OrderParentBean;

import org.xutils.common.Callback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TlayIndentViewPagercontentFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private OrderParentBean orderParentBean = new OrderParentBean();
    private ListView lv;
    private ArrayList<OrderBean> orderBeens;
    private SwipeRefreshLayout srl;
    private String date;
    private AnimalAdapter animalAdapter;

    public static TlayIndentViewPagercontentFragment newInstance(String param1) {
        TlayIndentViewPagercontentFragment fragment = new TlayIndentViewPagercontentFragment();
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
        View view = inflater.inflate(R.layout.fragment_tlay_indent_view_pagercontent, container, false);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl_order);
        lv = (ListView) view.findViewById(R.id.indent_list);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //获取当前年月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        date = sdf.format(new java.util.Date());
        //SwipeRefreshLayout下拉刷新事件
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mParam1.equals("1")) {
                    NetWorkTools.arequestGrab_single_table("1", "5", new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            orderParentBean = JSON.parseObject(result, OrderParentBean.class);
                            orderBeens = orderParentBean.getData();
                            animalAdapter = new AnimalAdapter(getActivity(), orderBeens);
                            lv.setAdapter(animalAdapter);
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {

                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                } else {
                    NetWorkTools.requestOrderhistory("1", "15", mParam1, date, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            orderParentBean = JSON.parseObject(result, OrderParentBean.class);
                            orderBeens = orderParentBean.getData();
                            animalAdapter = new AnimalAdapter(getActivity(), orderBeens);
                            lv.setAdapter(animalAdapter);
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {

                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }
                //关闭动画
                srl.setRefreshing(false);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mParam1.equals("1")) {

                } else if (mParam1.equals("2")) {
                    Intent intent = new Intent();
                    intent.putExtra("orderBeens", orderBeens.get(i));
                    intent.setAction("android.intent.szg.Service_Order_content");
                    startActivity(intent);
                } else if (mParam1.equals("5")) {
                    Intent intent = new Intent();
                    intent.putExtra("orderBeens", orderBeens.get(i));
                    intent.setAction("android.intent.szg.History_order_centen");
                    startActivity(intent);
                } else if (mParam1.equals("6")) {
                    Intent intent = new Intent();
                    intent.putExtra("orderBeens", orderBeens.get(i));
                    intent.setAction("android.intent.szg.History_order_centen");
                    startActivity(intent);
                }
            }
        });
    }
    public class AnimalAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<OrderBean> orderBeen;

        public AnimalAdapter() {
            super();
        }

        public AnimalAdapter(Context context, ArrayList<OrderBean> orderBeen) {
            this.context = context;
            this.orderBeen = orderBeen;
        }

        @Override
        public int getCount() {
            return orderBeen.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.order_history_list_content, viewGroup, false);
            TextView tvIndent_serviceclass = (TextView) view.findViewById(R.id.indent_serviceclass);
            TextView tvIndent_creationDate = (TextView) view.findViewById(R.id.indent_creationDate);
            TextView tvIndent_addressArea = (TextView) view.findViewById(R.id.indent_addressArea);
            TextView tvIndent_servicestate = (TextView) view.findViewById(R.id.indent_servicestate);
            tvIndent_serviceclass.setText(orderBeen.get(i).getServiceclass());
            tvIndent_creationDate.setText(orderBeen.get(i).getCreationDate());
            tvIndent_addressArea.setText(orderBeen.get(i).getAddressArea());
            int tate = orderBeen.get(i).getState();
            switch (tate) {
                case 1:
                    tvIndent_servicestate.setText("竟单中！");
                    break;
                case 2:
                    tvIndent_servicestate.setText("已生效待服务！");
                    break;
                case 5:
                    tvIndent_servicestate.setText("等待确认支付！");
                    break;
                case 6:
                    tvIndent_servicestate.setText("已完成！");
                    break;
            }
            return view;
        }
    }
}
