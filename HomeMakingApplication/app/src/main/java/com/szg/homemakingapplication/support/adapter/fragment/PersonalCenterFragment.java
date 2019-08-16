package com.szg.homemakingapplication.support.adapter.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.szg.homemakingapplication.MyApplication;
import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.UI.Login.LogIn;
import com.szg.homemakingapplication.accesstool.NetWorkTools;
import com.szg.homemakingapplication.model.BasicServiceBean;
import com.szg.homemakingapplication.model.BasicServiceParentBean;
import com.szg.homemakingapplication.model.Bean;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.List;

public class PersonalCenterFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private ImageView ivicon;
    private ListView lv;
    private TextView tvService_times_this_month,tvThis_month_income,tvReceived_several,tvbalance_of_account;
    public static PersonalCenterFragment newInstance(String param1) {
        PersonalCenterFragment fragment = new PersonalCenterFragment();
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
        View view=inflater.inflate(R.layout.fragment_personal_center, container, false);
        lv= (ListView) view.findViewById(R.id.personal_list);
        ivicon= (ImageView) view.findViewById(R.id.user_icon);
        //下载图片
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();
        x.image().bind(ivicon,MyApplication.userBean.getIcon(),imageOptions);
        AnimalAdapter animalAdapter=new AnimalAdapter(getContext());
        lv.setAdapter(animalAdapter);
        tvService_times_this_month= (TextView) view.findViewById(R.id.Service_times_this_month);
        tvThis_month_income= (TextView) view.findViewById(R.id.This_month_income);
        tvReceived_several= (TextView) view.findViewById(R.id.Received_several);
        tvbalance_of_account= (TextView) view.findViewById(R.id.balance_of_account);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //获取当前年月
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        String date=sdf.format(new java.util.Date());
        //基本服务情况
        NetWorkTools.requestBasic_service(date, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BasicServiceParentBean basicServiceParentBean= JSON.parseObject(result,BasicServiceParentBean.class);
                BasicServiceBean basicServiceBean=basicServiceParentBean.getData();
                tvReceived_several.setText("本月好评数："+basicServiceBean.getGoodJudgeAmount()+"个");
                tvService_times_this_month.setText("本月服务数："+basicServiceBean.getFinishedOrderAmout()+"次");
                tvbalance_of_account.setText("账户余额："+basicServiceBean.getBalance()+"元");
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
        //月收入总额
        NetWorkTools.requestTotal_monthly_income(date, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Bean bean= JSON.parseObject(result,Bean.class);
                tvThis_month_income.setText("本月收益："+bean.getData()+"元");
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent intent=new Intent("android.intent.szg.MessageActivity");
                        startActivity(intent);
                        break;
                    case 1:
                        intent=new Intent("android.intent.szg.BillActivity");
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        intent=new Intent("android.intent.szg.Change_the_password");
                        startActivity(intent);
                        break;
                    case 4:
                        intent=new Intent(PersonalCenterFragment.this.getActivity(), LogIn.class);
                        SharedPreferences sp = getActivity().getSharedPreferences("mysp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("RememberPwd",false).commit();
                        editor.putBoolean("AutoLogin",false).commit();
                        startActivity(intent);
                        break;
                    case 5:
                        intent=new Intent("android.intent.szg.Submit_feedback_Activity");
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    public class AnimalAdapter extends BaseAdapter {
        private Context context;
        private String[] personalcontent=new String[]{"通知","账单","升级","修改密码","退出登录","提交意见反馈"};
        public AnimalAdapter() {
            super();
        }

        public AnimalAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return personalcontent.length;
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
            view= LayoutInflater.from(context).inflate(R.layout.personal_list_content,viewGroup,false);
            TextView title= (TextView) view.findViewById(R.id.personal_list_name);
            title.setText(personalcontent[i]);
            return view;
        }
    }
}
