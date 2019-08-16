package com.example.administrator.signaturetest.GroupList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.signaturetest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 * 分组列表
 */

public class GroupListActivity extends Activity {
    private GroupListAdapter adapter = null;
    private ListView listView = null;
    private List<String> list = new ArrayList<String>();
    private List<String> listTag = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_activity);
        setData();
        adapter = new GroupListAdapter(this, list, listTag);
        listView = (ListView) findViewById(R.id.group_list);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        Toast.makeText(getApplicationContext(), "公司必修", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "个人贡献", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "我要提问", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(), "我要回答", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(getApplicationContext(), "我要考试", Toast.LENGTH_SHORT).show();
                        break;
                    case 9:
                        Toast.makeText(getApplicationContext(), "申请成为圈子管理员", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public void setData() {
        list.add("A");
        listTag.add("A");
        list.add("公司必修");
        list.add("个人贡献");

        list.add("B");
        listTag.add("B");
        list.add("我要提问");
        list.add("我要回答");

        list.add("C");
        listTag.add("C");
        list.add("我要考试");

        list.add("d");
        listTag.add("d");
        list.add("申请成为圈子管理员");

    }

    private static class GroupListAdapter extends ArrayAdapter<String> {
        private List<String> listTag = null;

        public GroupListAdapter(Context context, List<String> objects, List<String> tags) {
            super(context, 0, objects);
            this.listTag = tags;
        }

        @Override
        public boolean isEnabled(int position) {
            if (listTag.contains(getItem(position))) {
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (listTag.contains(getItem(position))) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.group_list_item_tag, null);
            } else {
                view = LayoutInflater.from(getContext()).inflate(R.layout.group_list_item, null);
            }
            TextView textView = (TextView) view.findViewById(R.id.group_list_item_text);
            textView.setText(getItem(position));
            return view;
        }
    }
}