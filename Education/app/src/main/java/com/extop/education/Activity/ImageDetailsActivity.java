package com.extop.education.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.extop.education.Adapter.MyImageView;
import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.R;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 * 图片详情
 * URL转Bitmap不能在主线程中使用没找到解决方法暂时不启用
 */

public class ImageDetailsActivity extends AppCompatActivity {
    private ViewPager viewPager;
    //给ViewPager设置数据源和适配器
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private List<MyImageView> myImageViews = new ArrayList<MyImageView>();
    private MyImageView myImageView = null;
    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private String [] img_url=null;
    private String img="";
    Intent intent;
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    viewPager.setVisibility(View.VISIBLE);
                    for (int i=0;i<img_url.length;i++){
                        System.out.println(img_url[i]);
                        System.out.println(NetWorkTools.getURLImage(img_url[i]));
//                        Log.d("img_url",img_url[i]);
//                        Log.d("bitmap", NetWorkTools.getURLImage(img_url[i])+"");
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);
        //获得viewPager对象
        viewPager = (ViewPager) findViewById(R.id.my_img_view_pager);
        toolbar= (Toolbar) findViewById(R.id.my_img_icon);
        //给Toolbar设置返回图标
        toolbar.setNavigationIcon(R.mipmap.arrow_left_d);
        //返回图标设置点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent=getIntent();
        img_url=intent.getStringArrayExtra("img_src");
//        initArray(img_url);
        //初始化pagerAdapter
        pagerAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(myImageViews.get(position));
                return myImageViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Log.d("position", position + "");
                if (position < myImageViews.size()) {
                    container.removeView(myImageViews.get(position));
                }
            }

            @Override
            public int getCount() {
                return myImageViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
//        viewPager.setAdapter(pagerAdapter);
    }
    private void initArray(final String[] img_url){
        /*new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (int i=0;i<img_url.length;i++){
                    Log.d("img_url",img_url[i]);
                    bitmaps.add(NetWorkTools.getURLImage(img_url[i]));
                    myImageView = new MyImageView(getApplicationContext());
                    myImageView.setImageBitmap(NetWorkTools.getURLImage(img_url[i]));
                    myImageViews.add(myImageView);
                }
                Message msg = new Message();
                msg.what = 0;
//                    msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();*/
        Log.d("ss", "111");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
//                    msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();
    }
}
