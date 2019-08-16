package com.example.administrator.firstlineofcodetest.MyImage;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.administrator.firstlineofcodetest.R;

/**
 * Created by Administrator on 2018/1/21.
 * 自定义图片展示布局
 */

public class MyImageActivity extends AppCompatActivity {
    private ViewPager viewPager;
    //给ViewPager设置数据源和适配器
    private int[] mImages = new int[]{R.drawable.test, R.drawable.test2, R.drawable.test3, R.drawable.test4};
    private MyImageView[] myImageViews=new MyImageView[mImages.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vp);
        viewPager = (ViewPager) findViewById(R.id.ViewPager);
        //给viewPager设置适配器
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                MyImageView myImageView=new MyImageView(getApplicationContext());
                myImageView.setImageResource(mImages[position]);
                container.addView(myImageView);
                myImageViews[position]=myImageView;
                return myImageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                if(position<myImageViews.length){
                    container.removeView(myImageViews[position]);
                }
            }

            @Override
            public int getCount() {
                return myImageViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });
    }
}
