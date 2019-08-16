package com.extop.HangTianTianQiTong.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.extop.HangTianTianQiTong.Adapter.MyImageView;
import com.extop.HangTianTianQiTong.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageShowActivity extends AppCompatActivity {
    private ViewPager viewPager;
    //给ViewPager设置数据源和适配器
    private String[] mImages ;
    private MyImageView[] myImageViews;
//    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_show);
        Intent intent=getIntent();
        mImages=intent.getStringArrayExtra("img_src");
        myImageViews=new MyImageView[mImages.length];
        viewPager = (ViewPager) findViewById(R.id.video_image);
//        dialog = new ProgressDialog(this);
        //给viewPager设置适配器

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.d("加载位置", position+"");
                final MyImageView myImageView=new MyImageView(getApplicationContext());
                //方法一通过Glide库加载图片
                Glide.with(getApplicationContext()).load(mImages[position]).into(myImageView);
//                    dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
//                    dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//                    dialog.setTitle("资源加载中,请稍后...");
//                    dialog.show();
                //方法二通过xutils加载图片
                /*x.image().loadDrawable(mImages[position], null, new Callback.CommonCallback<Drawable>() {
                    @Override
                    public void onSuccess(Drawable result) {
                        Log.d("加载的图片",result+"");
                        myImageView.setImageDrawable(result);
//                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.d("加载图片错误", ex+"");
                        Log.d("回调信息", isOnCallback+"");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                    }
                });*/
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
    //按返回键事件
    @Override
    public void onBackPressed() {
            finish();
    }
}
