package com.example.administrator.signaturetest.MyImage;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.example.administrator.signaturetest.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ScaleImageViewActivity extends AppCompatActivity {
    protected ScaleImageView scaleImageView;//加载图片控件
    //缩放图片所需的变量
    private String imgUrl = "http://59.110.226.225/upload/1487234777436_0.png";
    private ProgressDialog pd;
    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.cancel();
                    scaleImageView.setVisibility(View.VISIBLE);
                    Bitmap bmp = (Bitmap) msg.obj;
                    scaleImageView.setImageBitmap(bmp);
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_image_view);
        //获取背景图片
        scaleImageView= (ScaleImageView) findViewById(R.id.image2);
        loadingPictures(imgUrl);
    }
    //显示对话框并加载图片
    public void loadingPictures(String imgUrls) {
        //缩放图片的方法
        imgUrl = imgUrls;
        pd = new ProgressDialog(ScaleImageViewActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        pd.setMessage("资源加载中,请稍后...");// 设置ProgressDialog提示信息
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        pd.setIndeterminate(false);
        pd.setCancelable(false); // 设置ProgressDialog 是否可以按退回键取消
        pd.show(); // 让ProgressDialog显示
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Bitmap bmp = getURLImage(imgUrl);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();
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

    //用户点返回键时直接关闭当前活动
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int visibility = scaleImageView.getVisibility();
        /*View.VISIBLE 常量值为0，意思是可见的
        *View.INVISIBLE 常量值为4，意思是不可见的
        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
        * */
        finish();
        return false;
    }
}
