package com.extop.beikongsanitation.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.extop.beikongsanitation.MyApplication;
import com.extop.beikongsanitation.R;
import com.extop.beikongsanitation.Tool.MyAdapter;
import com.extop.beikongsanitation.Tool.NetWorkTools;

import org.xutils.common.Callback;

import java.util.ArrayList;
/**操作记录**/
public class OperationRecordsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private ListView lv_personal;//菜单列表
    private ArrayList<String> arrayList;//存储菜单名称
    private Intent intent;
    private TextView driverName,driverCode;
    public static OperationRecordsFragment newInstance(String param1) {
        OperationRecordsFragment fragment = new OperationRecordsFragment();
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
        View view=inflater.inflate(R.layout.fragment_operation_records, container, false);
        driverName=view.findViewById(R.id.driverName);
        driverName.setText(MyApplication.driverName);
        driverCode=view.findViewById(R.id.driverCode);
        driverCode.setText(MyApplication.driverCode);

        lv_personal = (ListView) view.findViewById(R.id.personal_list);
        arrayList = new ArrayList<String>();
        arrayList.add("登录记录");
        arrayList.add("加水记录");
        arrayList.add("排污记录");
        arrayList.add("倾倒记录");
        if (MyApplication.typeStatus){
            arrayList.add("垃圾收集记录");
        }
        MyAdapter myAdapter = new MyAdapter(getContext(), arrayList, R.layout.personal_center_listitem);
        lv_personal.setAdapter(myAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //列表点击事件
        lv_personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        //登录记录
                        intent = new Intent("android.intent.Activity.RecordDetailsActivity");
                        intent.putExtra("operateTypeId",0);
                        startActivity(intent);
                        Log.d("点击","登录记录");
                        break;
                    case 1:
                        //加水记录
                        intent = new Intent("android.intent.Activity.RecordDetailsActivity");
                        intent.putExtra("operateTypeId",1);
                        startActivity(intent);
                        Log.d("点击","加水记录");
                        break;
                    case 2:
                        //排污记录
                        intent = new Intent("android.intent.Activity.RecordDetailsActivity");
                        intent.putExtra("operateTypeId",2);
                        startActivity(intent);
                        Log.d("点击","排污记录");
                        break;
                    case 3:
                        //倾倒记录
                        intent = new Intent("android.intent.Activity.RecordDetailsActivity");
                        intent.putExtra("operateTypeId",3);
                        startActivity(intent);
                        Log.d("点击","倾倒记录");
                        break;
                    case 4:
                        //垃圾收集记录
                        intent = new Intent("android.intent.Activity.GarbageCollectorDetailsActivity");
                        intent.putExtra("operateTypeId",4);
                        startActivity(intent);
                        Log.d("点击","倾倒记录");
                        break;
                }
            }
        });
    }
}
