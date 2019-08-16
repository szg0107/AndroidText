package com.example.administrator.xianxiaapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

import com.example.administrator.xianxiaapplication.Instance.ArticleBean;
import com.example.administrator.xianxiaapplication.Instance.News;
import com.example.administrator.xianxiaapplication.R;
import com.example.administrator.xianxiaapplication.WebViewActivity;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class Collect_GreyContentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private ArticleBean articleBean;
    AnimalAdapter aa;
    private List<ArticleBean> listds = new ArrayList<ArticleBean>();

    public static Collect_GreyContentFragment newInstance(String param1) {
        Collect_GreyContentFragment fragment = new Collect_GreyContentFragment();
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
        View view = inflater.inflate(R.layout.fragment_collect__grey_content, container, false);
        ListView lv = (ListView) view.findViewById(R.id.collect_greylist);

        aa = new AnimalAdapter(listds, getActivity());
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                ArticleBean articleBean = listds.get(i);
                intent.putExtra("url", articleBean);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        myDBHelper = new MyDBOpenHelper(getContext(), "Mydata.db", null, 1);
        db = myDBHelper.getWritableDatabase();
        listds.clear();
        Cursor cursor = db.query("ArticleBean", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                articleBean = new ArticleBean();
                articleBean.setIs_collected(cursor.getInt(cursor.getColumnIndex("is_collected")));
                articleBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                articleBean.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                articleBean.setSmall_image(cursor.getString(cursor.getColumnIndex("small_image")));
                listds.add(articleBean);
            } while (cursor.moveToNext());
        }
        aa.notifyDataSetChanged();
        cursor.close();
    }

    public class AnimalAdapter extends BaseAdapter {
        private List<ArticleBean> listd;
        private Context context;

        public AnimalAdapter() {
            super();
        }

        public AnimalAdapter(List<ArticleBean> listd, Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.home_list_content, viewGroup, false);
            TextView title = (TextView) view.findViewById(R.id.news_title);
            title.setText(listd.get(i).getTitle());
            ImageView icon = (ImageView) view.findViewById(R.id.news_icon);
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
            x.image().bind(icon, listd.get(i).getSmall_image(), imageOptions);
            return view;
        }
    }

    public class MyDBOpenHelper extends SQLiteOpenHelper {
        public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            //1.上下文 2.数据库名字 3.   4.版本号
            super(context, "Mydata.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE ArticleBean(articleBeanid INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR(200),url VARCHAR(200),small_image VARCHAR(200),is_collected INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("ALTER TABLE ArticleBean ADD phone VARCHAR(12) NULL");
        }
    }
}
