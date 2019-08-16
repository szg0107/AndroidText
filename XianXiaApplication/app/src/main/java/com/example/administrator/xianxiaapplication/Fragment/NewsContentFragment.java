package com.example.administrator.xianxiaapplication.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.xianxiaapplication.Instance.DailyInstance;
import com.example.administrator.xianxiaapplication.Instance.News;
import com.example.administrator.xianxiaapplication.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class NewsContentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private  AppCompatActivity ACA;
    private ListView lv;
    public static NewsContentFragment newInstance(String param1) {
        NewsContentFragment fragment = new NewsContentFragment();
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
        View view=inflater.inflate(R.layout.fragment_news_content, container, false);
        lv= (ListView) view.findViewById(R.id.news_list);
        ACA = (AppCompatActivity) getActivity();
        try {
            InputStream is =  ACA.getAssets().open(mParam1);
            List<News> listd = getNewses(is);
            AnimalAdapter aa=new AnimalAdapter(listd,ACA);
            lv.setAdapter(aa);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //onTestBaidu2Click();
        return view;
    }
    //网络获取
    private void onTestBaidu2Click() {
        RequestParams params = new RequestParams("http://www.xinhuanet.com/mil/news_mil.xml");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
                try {
                    //List<News> listd = getNewses(result);
                    //AnimalAdapter aa=new AnimalAdapter(listd,ACA);
                    //lv.setAdapter(aa);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //ACA = (AppCompatActivity) getActivity();
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
    public static ArrayList<News> getNewses(InputStream xml)throws Exception{
        //InputStream xml String result)
        ArrayList<News> newses=null;
        News news=null;
        // 创建一个xml解析的工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // 获得xml解析类的引用
        XmlPullParser parser = factory.newPullParser();
        //parser.setInput(new StringReader(result));
        parser.setInput(xml, "UTF-8");
        // 获得事件的类型
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    newses = new ArrayList<News>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("item".equals(parser.getName())) {
                        news = new News();
                        // 取出属性值
                    }else if ("title".equals(parser.getName())){
                        String title=parser.nextText();
                        if(news != null ){
                            news.setTitle(title);
                        }
                    } else if ("link".equals(parser.getName())) {
                        String link = parser.nextText();// 获取该节点的内容
                        if(news !=null) {
                            news.setLink(link);
                        }
                    } else if ("description".equals(parser.getName())) {
                        String description =parser.nextText();
                        if(news !=null){
                            news.setDescription(description);
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("item".equals(parser.getName())) {
                        newses.add( news);
                        news = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return  newses;
    }
    public class AnimalAdapter extends BaseAdapter {
        private List<News> listd;
        private Context context;

        public AnimalAdapter() {
            super();
        }

        public AnimalAdapter(List<News> listd, Context context) {
            this.listd = listd;
            this.context = context;
        }

        @Override
        public int getCount() {
            return listd.size();
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
            view= LayoutInflater.from(context).inflate(R.layout.newslistcontent,viewGroup,false);
            TextView title= (TextView) view.findViewById(R.id.home_title);
            TextView description= (TextView) view.findViewById(R.id.home_description);
            title.setText(listd.get(i).getTitle());
            description.setText(listd.get(i).getDescription());
            return view;
        }
    }
}
