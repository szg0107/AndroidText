package com.szg.homemakingapplication.UI.PersonalCenterActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.accesstool.NetWorkTools;
import com.szg.homemakingapplication.model.Bean;
import com.szg.homemakingapplication.model.NoticeBean;
import com.szg.homemakingapplication.model.NoticeParentBean;
import com.szg.homemakingapplication.model.OrderParentBean;
import com.szg.homemakingapplication.support.adapter.fragment.TlayIndentViewPagercontentFragment;

import org.xutils.common.Callback;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/12.
 */

public class MessageActivity extends AppCompatActivity {
    private ListView lv;
    private NoticeParentBean noticeParentBean=new NoticeParentBean();
    private ArrayList<NoticeBean> noticeBeen;
    private AnimalAdapter animalAdapter;
    private SwipeRefreshLayout srl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.message_list);
        Toolbar tbgoback= (Toolbar) findViewById(R.id.toolbar_message_listc);
        srl= (SwipeRefreshLayout) findViewById(R.id.srl_message);
        tbgoback.setNavigationIcon(R.drawable.arrow_left_d);
        tbgoback.setTitle("通知列表");
        //点导航栏图标退出
        tbgoback.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        lv = (ListView) findViewById(R.id.message_ListView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetWorkTools.requestGet_notification_list(new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        noticeParentBean= JSON.parseObject(result,NoticeParentBean.class);
                        animalAdapter=new AnimalAdapter(getApplication(),noticeParentBean.getData());
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
                //关闭动画
                srl.setRefreshing(false);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent("android.intent.szg.MessageContentActivity");
                intent.putExtra("NoticeBean",noticeParentBean.getData().get(i));
                startActivity(intent);
            }
        });
    }

    public class AnimalAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<NoticeBean> noticeBeen;
        public AnimalAdapter() {
            super();
        }

        public AnimalAdapter(Context context,ArrayList<NoticeBean> noticeBeen) {
            this.context = context;
            this.noticeBeen=noticeBeen;
        }

        @Override
        public int getCount() {
            return noticeBeen.size();
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
            view= LayoutInflater.from(context).inflate(R.layout.message_list_item,viewGroup,false);
            TextView title= (TextView) view.findViewById(R.id.message_title);
            TextView pushtime= (TextView) view.findViewById(R.id.message_pushtime);
            title.setText(noticeBeen.get(i).getTitle());
            pushtime.setText(noticeBeen.get(i).getPushtime());
            return view;
        }
    }
}
