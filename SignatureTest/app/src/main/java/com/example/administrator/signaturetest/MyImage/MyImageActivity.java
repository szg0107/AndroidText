package com.example.administrator.signaturetest.MyImage;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.example.administrator.signaturetest.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyImageActivity extends AppCompatActivity {
    private ViewPager viewPager;
    //给ViewPager设置数据源和适配器
    private int[] mImages = new int[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    private MyImageView[] myImageViews=new MyImageView[mImages.length];


    private String[] mImages2=new String[]{"http://123.56.187.157:80/hlts/upload/1520993185967.jpg",
            "http://123.56.187.157:80/hlts/upload/1520993339901.jpg",
            "http://123.56.187.157:80/hlts/upload/1520993446515.jpg",
            "http://123.56.187.157:80/hlts/upload/1520993469154.jpg",
            "http://123.56.187.157:80/hlts/upload/1520993657659.jpg"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_image_layout);
        viewPager = (ViewPager) findViewById(R.id.ViewPager);
        //给viewPager设置适配器

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.d("加载位置", position+"");
                final MyImageView myImageView=new MyImageView(getApplicationContext());
                //加载本地图片
//                myImageView.setImageResource(mImages[position]);
                //xutils加载网络图片
                /*x.image().loadDrawable(mImages2[position], null, new Callback.CommonCallback<Drawable>() {
                    @Override
                    public void onSuccess(Drawable result) {
                        Log.d("加载的图片",result+"");
                        myImageView.setImageDrawable(result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.d("加载图片错误", ex+"");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });*/
                //Glide库加载网络图片
                Glide.with(getApplicationContext()).load(mImages[position]).into(myImageView);
                container.addView(myImageView);
                myImageViews[position]=myImageView;
                return myImageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                container.removeView(myImageViews.get(position));
                Log.d("删除位置", position+"");
                container.removeView(myImageViews[position]);
            }

            @Override
            public int getCount() {
//                return myImageViews.size();
//                return myImageViews.length;
                return myImageViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });
    }
    //将图片URL转换为Bitmap
    public Bitmap getURLImage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
