package com.extop.education.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.Adapter.ScaleImageView;
import com.extop.education.MyApplication;
import com.extop.education.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.extop.education.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/4/11.
 * 圈内搜索文案视频等
 */

public class CircleInsideSearch extends AppCompatActivity {
    TextView tv_video,tv_frequency,tv_pdf,tv_Image,tv_videotitle;
    Toolbar tb_videotitle;
    XWalkView wv_VideoDetails;
    Intent intent;
    String type="avi",content="",reuse="";
    String[] typeArray = {"avi", "png", "mp3", "pdf"}, name,number;
    private boolean isOpen = true;
    Object[] controlsArray = {tv_video, tv_frequency, tv_pdf, tv_Image};

    private ScaleImageView scaleImageView;
    private String imgurl = "";
    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    scaleImageView.setVisibility(View.VISIBLE);
                    Bitmap bmp=(Bitmap)msg.obj;
                    scaleImageView.setImageBitmap(bmp);
                    break;
            }
        };
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Window window=getWindow();
        NetWorkTools.taskbar_transparent(window,this);


        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_inside_search);
        //键盘弹起窗口可拉伸
        assistActivity(this);
        //获取控件
        tb_videotitle= (Toolbar) findViewById(R.id.video_icon);
        tv_videotitle= (TextView) findViewById(R.id.video_title);
        tv_video= (TextView) findViewById(R.id.tv_video);
        tv_frequency= (TextView) findViewById(R.id.tv_frequency);
        tv_pdf= (TextView) findViewById(R.id.tv_pdf);
        tv_Image= (TextView) findViewById(R.id.tv_Image);
        wv_VideoDetails= (XWalkView) findViewById(R.id.wv_VideoDetails);
        scaleImageView= (ScaleImageView) findViewById(R.id.video_image);

        intent=getIntent();
        content=intent.getStringExtra("content");
        typeArray=intent.getStringArrayExtra("typeArray");
        name=intent.getStringArrayExtra("name");
        number=intent.getStringArrayExtra("number");
        isOpen=intent.getBooleanExtra("isOpen",false);
        reuse=intent.getStringExtra("reuse");
        controlsArray[0] = tv_video;
        controlsArray[1] = tv_Image;
        controlsArray[2] = tv_frequency;
        controlsArray[3] = tv_pdf;

        //设置textView的值和显示与隐藏
        for (int i = 0; i < name.length; i++) {
            TextView textView = (TextView) controlsArray[i];
            textView.setText(name[i]);
            if (number[i].equals("0")) {
                textView.setVisibility(View.GONE);
            }
        }

        //根据textView是否隐藏设置第一个没隐藏的textView为选中状态并设置类型
        for (int i=0;i<controlsArray.length;i++){
            TextView textView = (TextView) controlsArray[i];
            int visibility=textView.getVisibility();
            if (visibility==0){
                textView.setSelected(true);
                type = typeArray[i];
                break;
            }
        }
        //设置标题、设置返回按钮并添加监听事件
        tv_videotitle.setText("搜索结果");
        tb_videotitle.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_videotitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //默认选中视频  给视频、音频、文案、图片绑定单击事件
        tv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_video.setSelected(true);
                type=typeArray[0];
                wv_VideoDetails.loadUrl(getPage(isOpen,type));
                Log.v("url",getPage(isOpen,type));

            }
        });
        tv_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_Image.setSelected(true);
                type=typeArray[1];
                wv_VideoDetails.loadUrl(getPage(isOpen,type));
                Log.v("url",getPage(isOpen,type));
            }
        });
        tv_frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_frequency.setSelected(true);
                type=typeArray[2];
                wv_VideoDetails.loadUrl(getPage(isOpen,type));
                Log.v("url",getPage(isOpen,type));
            }
        });
        tv_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_pdf.setSelected(true);
                type=typeArray[3];
                wv_VideoDetails.loadUrl(getPage(isOpen,type));
                Log.v("url",getPage(isOpen,type));
            }
        });


        //webview设置属性
        NetWorkTools.XWalkView_Settings(wv_VideoDetails,this);
        wv_VideoDetails.setUIClient(new XWalkUIClient(wv_VideoDetails) {
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                dialog(message);
                result.cancel();
                return true;
            }
        });
        wv_VideoDetails.loadUrl(getPage(isOpen,type));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wv_VideoDetails != null) {
            wv_VideoDetails.pauseTimers();
            wv_VideoDetails.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wv_VideoDetails != null) {
            wv_VideoDetails.resumeTimers();
            wv_VideoDetails.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_VideoDetails != null) {
            wv_VideoDetails.onDestroy();
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
        wv_VideoDetails.getNavigationHistory().clear();
        if (visibility == 8 && keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        } else if (visibility == 0 && keyCode == KeyEvent.KEYCODE_BACK) {
            scaleImageView.setVisibility(View.GONE);
        }
        return false;
    }

    //得到当前按钮被选中的URL地址
    private String getPage(Boolean isOpen, String type) {
        String page = "";
        if (isOpen) {
            switch (type) {
                case "avi":
                    page = MyApplication.url + "no_img_videoList.view?w=a&type=avi&id=" + MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                    break;
                case "mp3":
                    page = MyApplication.url + "no_img_audio.view?w=a&type=mp3&id=" + MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                    break;
                case "pdf":
                    page = MyApplication.url + "no_img_courseware.view?w=a&type=pdf&id=" + MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                    break;
                case "png":
                    page = MyApplication.url + "no_img_picture.view?w=a&type=png&id=" + MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                    break;
            }
        } else {
            switch (type) {
                case "avi":
                    page = MyApplication.url + "videoList.view?w=a&type=avi&id=" + MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                    break;
                case "mp3":
                    page = MyApplication.url + "audio.view?w=a&type=mp3&id=" + MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                    break;
                case "pdf":
                    page = MyApplication.url + "coursewareList.view?w=a&type=pdf&id=" + MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                    break;
                case "png":
                    page = MyApplication.url + "picture.view?w=a&type=png&id=" + MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                    break;
            }
        }

        return page;
    }
    //重置所有文本的选中状态
    private void setSelected() {
        tv_video.setSelected(false);
        tv_frequency.setSelected(false);
        tv_pdf.setSelected(false);
        tv_Image.setSelected(false);
    }

    protected void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CircleInsideSearch.this,R.style.AlertDialog);
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
    public void setValue(String string) {
        if(type.equals("pdf")){
            intent=new Intent("android.intent.Activity.PDFActivity");
            intent.putExtra("pdf",string);
            startActivity(intent);
        }else if(type.equals("avi")){
            intent=new Intent("android.intent.Activity.CoursewareDetailsActivity");
            intent.putExtra("courseware",string);
            startActivity(intent);
        }else if(type.equals("mp3")){
            intent=new Intent("android.intent.Activity.AudioDetailsActivity");
            intent.putExtra("audio",string);
            startActivity(intent);
        }else if(type.equals("png")){
            Log.i("测试", "图片地址: "+string);
            if (string.contains("http")) {
                imgurl = string;
            } else {
                imgurl = MyApplication.url + "upload/" + string;
            }
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

    }

    @JavascriptInterface
    public void isVIP(){
        dialogs();
    }

    protected void  dialogs(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CircleInsideSearch.this,R.style.AlertDialog);
        builder.setMessage("您不是本圈子会员！请进行充值！");
        builder.setPositiveButton("充值", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                intent=new Intent("android.intent.Activity.WeChat_Alipay_pay");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    public Bitmap getURLimage(String url) {
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
