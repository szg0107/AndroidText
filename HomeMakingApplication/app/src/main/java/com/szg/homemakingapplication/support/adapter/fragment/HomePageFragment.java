package com.szg.homemakingapplication.support.adapter.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;

import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.UI.PersonalCenterActivity.BillActivity;
import com.szg.homemakingapplication.accesstool.NetWorkTools;

import com.szg.homemakingapplication.model.OrderBean;
import com.szg.homemakingapplication.model.OrderParentBean;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import org.xutils.common.Callback;

import java.util.ArrayList;

public class HomePageFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private ListView lvhomePage;
    private AnimalAdapter adapter;
    private OrderParentBean orderParentBean;
    private ArrayList<OrderBean> orderBeens;
    private SwipeRefreshLayout srl;

    public static HomePageFragment newInstance(String param1) {
        HomePageFragment fragment = new HomePageFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        lvhomePage = (ListView) view.findViewById(R.id.home_page_list);
        srl= (SwipeRefreshLayout) view.findViewById(R.id.srl_home);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetWorkTools.requesFor_single_table(new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        orderParentBean = JSON.parseObject(result, OrderParentBean.class);
                        orderBeens = orderParentBean.getData();
                        adapter = new AnimalAdapter(getContext(), orderBeens);
                        lvhomePage.setAdapter(adapter);
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
                //关闭动画
                srl.setRefreshing(false);
            }
        });
        lvhomePage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent("android.intent.szg.Grab_a_single_centent");
                intent.putExtra("orderBeens",orderBeens.get(i));
                startActivity(intent);
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
            //return  1;
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
            tvIndent_serviceclass.setTextColor(Color.rgb(0, 0, 0));
            tvIndent_creationDate.setText(orderBeen.get(i).getCreationDate());
            tvIndent_creationDate.setTextColor(Color.rgb(0, 0, 0));
            tvIndent_addressArea.setText(orderBeen.get(i).getAddressArea());
            tvIndent_addressArea.setTextColor(Color.rgb(0, 0, 0));
            tvIndent_servicestate.setTextColor(Color.rgb(255, 0, 0));
            tvIndent_servicestate.setText("抢单");
            return view;
        }
    }
}
