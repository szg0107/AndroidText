package com.example.administrator.xianxiaapplication.Fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.xianxiaapplication.Instance.ArticleBean;
import com.example.administrator.xianxiaapplication.Instance.BookBean;
import com.example.administrator.xianxiaapplication.Instance.ReadingBean;
import com.example.administrator.xianxiaapplication.Instance.ScienceBeanInstance;
import com.example.administrator.xianxiaapplication.R;
import com.example.administrator.xianxiaapplication.ReadingWebViewActivity;
import com.example.administrator.xianxiaapplication.WebViewActivity;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;


public class ReadingContentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private ListView lv;
    private ReadingBean readingBean;
    String urls="https://api.douban.com/v2/book/search?q=";
    public static ReadingContentFragment newInstance(String param1) {
        ReadingContentFragment fragment = new ReadingContentFragment();
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
        View view=inflater.inflate(R.layout.fragment_reading_content, container, false);
        lv= (ListView) view.findViewById(R.id.reading_list);
        onTestBaidu2Click();
        return view;
    }
    private void onTestBaidu2Click(){
        RequestParams params=new RequestParams(urls+mParam1);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
                 readingBean= JSON.parseObject(result,ReadingBean.class);
                //ACA = (AppCompatActivity) getActivity();
                AnimalAdapter aa=new AnimalAdapter(readingBean,getActivity());
                lv.setAdapter(aa);
//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Intent intent=new Intent(getActivity(),ReadingWebViewActivity.class);
//                        BookBean bookBean=readingBean.getBooks()[i];
//                        intent.putExtra("book",bookBean);
//                        startActivity(intent);
//                    }
//                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }


        });
    }
    class AnimalAdapter extends BaseAdapter {
        private ReadingBean readingBean;
        private Context context;

        public AnimalAdapter() {
            super();
        }

        public AnimalAdapter(ReadingBean readingBean, Context context) {
            this.readingBean=readingBean;
            this.context = context;
        }

        @Override
        public int getCount() {
            return readingBean.getBooks().length;
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
            view= LayoutInflater.from(context).inflate(R.layout.reading_list_content,viewGroup,false);
            ImageView icon= (ImageView) view.findViewById(R.id.reading_icon);
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
            x.image().bind(icon,readingBean.getBooks()[i].getImage(), imageOptions);
            TextView title= (TextView) view.findViewById(R.id.book_title);
            title.setText(readingBean.getBooks()[i].getTitle());
            TextView author= (TextView) view.findViewById(R.id.book_author);
            int authorsize=readingBean.getBooks()[i].getAuthor().length;
            String authors="";
            for(int e=0;e<authorsize;e++){
                authors=authors+readingBean.getBooks()[i].getAuthor()[e].toString();
            }
            author.setText(authors);
            TextView pubdate= (TextView) view.findViewById(R.id.book_pubdate);
            pubdate.setText(readingBean.getBooks()[i].getPubdate());
            TextView pages= (TextView) view.findViewById(R.id.book_pages);
            pages.setText(readingBean.getBooks()[i].getPages());
            TextView price= (TextView) view.findViewById(R.id.book_price);
            price.setText(readingBean.getBooks()[i].getPrice());
            return view;
        }
    }
}
