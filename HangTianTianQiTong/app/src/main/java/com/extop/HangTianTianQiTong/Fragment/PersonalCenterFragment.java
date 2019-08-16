package com.extop.HangTianTianQiTong.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.extop.HangTianTianQiTong.Activity.Start_Animation;
import com.extop.HangTianTianQiTong.Adapter.MyAdapter;
import com.extop.HangTianTianQiTong.Adapter.SharedHelper;
import com.extop.HangTianTianQiTong.Adapter.Switch;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import java.util.ArrayList;
import java.util.Map;

//个人中心
public class PersonalCenterFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private ListView lv_personal;//菜单列表
    private ArrayList<String> arrayList;//存储菜单名称
    private TextView tv_username;//退出登录按钮
    private Intent intent;
    //isOpen无图模式是否开启 isCircleJGG圈子九宫格模式是否开启 isCircleDetailsJGG圈子详情九宫格模式是否开启
    private boolean isOpen = true, isCircleJGG = true, isCircleDetailsJGG = true;
    //aSwitch无图模式开关按钮 circleListSwitch圈子九宫格模式开关按钮 circleDetailsSwitch圈子详情九宫格模式开关按钮
    private Switch aSwitch, circleListSwitch, circleDetailsSwitch;
    //读取本地文件
    private SharedHelper sharedHelper;

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
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        tv_username = (TextView) view.findViewById(R.id.user_name);
        lv_personal = (ListView) view.findViewById(R.id.personal_list);
        arrayList = new ArrayList<String>();
        arrayList.add("个人信息");
        arrayList.add("修改密码");
        arrayList.add("我的收藏");
        arrayList.add("电子商务");
//      arrayList.add("缴费记录");
        MyAdapter myAdapter = new MyAdapter(getContext(), arrayList, R.layout.personal_center_listitem);
        lv_personal.setAdapter(myAdapter);
        aSwitch = (Switch) view.findViewById(R.id.mySwitch);
        circleListSwitch = (Switch) view.findViewById(R.id.circleListSwitch);
        circleDetailsSwitch = (Switch) view.findViewById(R.id.circleDetailsSwitch);
        readSharedHelper();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //退出登录按钮点击事件
        tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedHelper.save("", "");
                intent = new Intent(getContext(), Start_Animation.class);
                startActivity(intent);
                MyApplication.finishActivity();
                getActivity().finish();
            }
        });
        //列表点击事件
        lv_personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        //修改用户信息
                        intent = new Intent("android.intent.Activity.UpdateUserInfoActivity");
                        startActivity(intent);
                        break;
                    case 1:
                        //修改密码
                        intent = new Intent();
                        intent.setAction("android.intent.Activity.ChangePasswordActivity");
                        startActivity(intent);
                        break;
                    case 2:
                        //我的收藏
                        intent = new Intent();
                        intent.setAction("android.intent.Activity.MyCollectionActivity");
                        startActivity(intent);
                        break;
                    case 3:
                        //电子商务
                        intent = new Intent();
                        intent.setAction("android.intent.Activity.ElectronicCommerceActivity");
                        startActivity(intent);
                        break;
                    /*case 2:
                        //缴费记录
                        intent=new Intent();
                        intent.setAction("android.intent.Activity.PaymentRecordsActivity");
                        startActivity(intent);
                        break;*/
                }
            }
        });

        //仿ios的Switch
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOpen) {
                    aSwitch.close();
                    isOpen = false;
                    sharedHelper.setNoImages(isOpen);
//                    Toast.makeText(getContext(), "关闭", Toast.LENGTH_SHORT).show();
                } else {
                    aSwitch.open();
                    isOpen = true;
                    sharedHelper.setNoImages(isOpen);
//                    Toast.makeText(getContext(), "开启", Toast.LENGTH_SHORT).show();
                }
            }
        });
        circleListSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCircleJGG) {
                    circleListSwitch.close();
                    isCircleJGG = false;
                    sharedHelper.setIsCircleJGG(isCircleJGG);
//                    Toast.makeText(getContext(), "关闭", Toast.LENGTH_SHORT).show();
                } else {
                    circleListSwitch.open();
                    isCircleJGG = true;
                    sharedHelper.setIsCircleJGG(isCircleJGG);
//                    Toast.makeText(getContext(), "开启", Toast.LENGTH_SHORT).show();
                }
            }
        });
        circleDetailsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCircleDetailsJGG) {
                    circleDetailsSwitch.close();
                    isCircleDetailsJGG = false;
                    sharedHelper.setIsCircleDetailsJGG(isCircleDetailsJGG);
//                    Toast.makeText(getContext(), "关闭", Toast.LENGTH_SHORT).show();
                } else {
                    circleDetailsSwitch.open();
                    isCircleDetailsJGG = true;
                    sharedHelper.setIsCircleDetailsJGG(isCircleDetailsJGG);
//                    Toast.makeText(getContext(), "开启", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //读取本地文件
    public void readSharedHelper() {
        sharedHelper = new SharedHelper(getContext());
        Map<String, Object> data = sharedHelper.read();
        //是否开启无图
        if (data.get("NoImages").toString().equals("true")) {
            aSwitch.open();
            isOpen = true;
        } else {
            aSwitch.close();
            isOpen = false;
        }
        //圈子列表是否开启九宫格
        if (data.get("isCircleJGG").toString().equals("true")) {
            circleListSwitch.open();
            isCircleJGG = true;
        } else {
            circleListSwitch.close();
            isCircleJGG = false;
        }
        //圈子详情列表是否开启九宫格
        if (data.get("isCircleDetailsJGG").toString().equals("true")) {
            circleDetailsSwitch.open();
            isCircleDetailsJGG = true;
        } else {
            circleDetailsSwitch.close();
            isCircleDetailsJGG = false;
        }
    }
}
