package com.example.administrator.xianxiaapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.xianxiaapplication.Instance.DailyInstance;
import com.example.administrator.xianxiaapplication.R;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;


public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private  AppCompatActivity ACA;
    private ListView lv;
    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
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
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        lv= (ListView) view.findViewById(R.id.home_list);
        onTestBaidu2Click();
        return view;
    }
    private void onTestBaidu2Click(){
        RequestParams params=new RequestParams("http://news-at.zhihu.com/api/4/news/latest");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                DailyInstance dailyInstance= JSON.parseObject(result,DailyInstance.class);
                //ACA = (AppCompatActivity) getActivity();
                AnimalAdapter aa=new AnimalAdapter(dailyInstance,getActivity());
                lv.setAdapter(aa);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public class AnimalAdapter extends BaseAdapter {
        private DailyInstance dailyInstance;
        private Context context;

        public AnimalAdapter() {
            super();
        }

        public AnimalAdapter(DailyInstance dailyInstance, Context context) {
            this.dailyInstance=dailyInstance;
            this.context = context;
        }

        @Override
        public int getCount() {
            return dailyInstance.getStories().size();
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
            view= LayoutInflater.from(context).inflate(R.layout.home_list_content,viewGroup,false);
            TextView title= (TextView) view.findViewById(R.id.news_title);
            ImageView icon= (ImageView) view.findViewById(R.id.news_icon);
            title.setText(dailyInstance.getStories().get(i).getTitle());
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setSize(DensityUtil.dip2px(60), DensityUtil.dip2px(60))
                    .setRadius(DensityUtil.dip2px(5))
                    // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.mipmap.ic_launcher)
                    .setFailureDrawableId(R.mipmap.ic_launcher)
                    .build();
            x.image().bind(icon,dailyInstance.getStories().get(i).getImages()[0], imageOptions);
            return view;
        }
    }
}
