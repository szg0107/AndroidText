package com.extop.HangTianTianQiTong.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/27.
 * 通知详情
 */

public class InformTheDetailsActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url+"Inform_the_details.view?id=";

    //缩放图片所需的变量
    private String imgurl = "";
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        tw_title.setText("通知详情");
        xWalkView.setUIClient(new XWalkUIClient(xWalkView){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                dialog(message);
                result.cancel();
                return true;
            }
        });
        xWalkView.loadUrl(url+intent.getStringExtra("id"));//调用loadView方法为WebView加入链接
        Log.d("url", url+ intent.getStringExtra("id"));
    }
    //详情弹窗
    protected void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(InformTheDetailsActivity.this,R.style.AlertDialog);
        builder.setMessage(message);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    @JavascriptInterface
    public void setValue(String string,String type) {
        if (type.equals("pdf")){
            intent=new Intent("android.intent.Activity.PDFActivity");
            intent.putExtra("pdf",string);
            startActivity(intent);
        }else {
            if (string.contains("http")) {
                imgurl = string;
            } else {
                imgurl = MyApplication.url + "upload/" + string;
            }
            loadingPictures(imgurl);
        }

    }

    //显示对话框并加载图片
    public  void loadingPictures(String imgurls){
        //缩放图片的方法
        imgurl=imgurls;
        pd=new ProgressDialog(InformTheDetailsActivity.this);
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
                Bitmap bmp = getURLimage(imgurl);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();
    }


    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(true);//不缓存
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
        int visibility=scaleImageView.getVisibility();
        /*View.VISIBLE 常量值为0，意思是可见的
        *View.INVISIBLE 常量值为4，意思是不可见的
        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
        * */
        if (visibility==8){
            finish();
        }else if (visibility==0){
            scaleImageView.setVisibility(View.GONE);
        }
    }

    //用户点返回键时直接关闭当前活动
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int visibility = scaleImageView.getVisibility();
        /*View.VISIBLE 常量值为0，意思是可见的
        *View.INVISIBLE 常量值为4，意思是不可见的
        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
        * */
        xWalkView.getNavigationHistory().clear();
        if (visibility == 8 && keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        } else if (visibility == 0 && keyCode == KeyEvent.KEYCODE_BACK) {
            scaleImageView.setVisibility(View.GONE);
        }
        return false;
    }
}
