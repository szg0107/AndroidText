package com.example.administrator.signaturetest.MyImage;

import android.graphics.drawable.Animatable;
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

import com.example.administrator.signaturetest.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;


import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by szg on 2018/3/15.
 * Fresco(图片加载库)+PhotoDraweeView(图片缩放库)+ViewPager(多图片加载)实现加载大图与图片缩放
 */

public class PhotoDraweeViewTextActivity extends AppCompatActivity {
    private ViewPager viewPager;
    //给ViewPager设置数据源和适配器
    private Uri[] mImages2=new Uri[5];
    private SimpleDraweeView[] draweeViews=new SimpleDraweeView[mImages2.length];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_image_layout);
        viewPager = (ViewPager) findViewById(R.id.ViewPager);
        mImages2[0]= Uri.parse("http://59.110.226.225/upload/1487238898426.png");
        mImages2[1]= Uri.parse("http://59.110.226.225/upload/1487238418124.png");
        mImages2[2]= Uri.parse("http://59.110.226.225/upload/1487238463160.png");
        mImages2[3]= Uri.parse("http://59.110.226.225/upload/1487238635131.png");
        mImages2[4]= Uri.parse("http://59.110.226.225/upload/1487238830264.png");
        //给viewPager设置适配器
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.d("加载位置", position+"");
                Log.d("图片Uri", mImages2[position]+"");

                final PhotoDraweeView mPhotoDraweeView=new PhotoDraweeView(getApplicationContext());
                PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
                controller.setUri(mImages2[position]);//设置图片url
                controller.setOldController(mPhotoDraweeView.getController());
                controller.setControllerListener(new BaseControllerListener<ImageInfo>(){
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (imageInfo == null || mPhotoDraweeView == null) {
                            return;
                        }
                        mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                    }
                });
                mPhotoDraweeView.setController(controller.build());
                container.addView(mPhotoDraweeView);
                draweeViews[position]=mPhotoDraweeView;
                return mPhotoDraweeView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                container.removeView(myImageViews.get(position));
                Log.d("删除位置", position+"");
                container.removeView(draweeViews[position]);
            }

            @Override
            public int getCount() {
                return draweeViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });
    }
}
