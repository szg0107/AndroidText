package com.extop.HangTianTianQiTong.Activity;

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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.extop.HangTianTianQiTong.Adapter.NetWorkTools;
import com.extop.HangTianTianQiTong.Adapter.ScaleImageView;
import com.extop.HangTianTianQiTong.Adapter.SharedHelper;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.extop.HangTianTianQiTong.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/7/3.
 * 我的收藏
 */

public class MyCollectionActivity extends AppCompatActivity {
    TextView tv_video, tv_frequency, tv_pdf, tv_Image, tv_title;
    Toolbar tb_toolbar;
    XWalkView wv_VideoDetails;
    Intent intent;
    String type = "avi";
    Boolean isCircle = false;
    private ScaleImageView scaleImageView;
    private String imgurl = "";
    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    scaleImageView.setVisibility(View.VISIBLE);
                    Bitmap bmp = (Bitmap) msg.obj;
                    scaleImageView.setImageBitmap(bmp);
                    break;
            }
        }

        ;
    };

    private SharedHelper sharedHelper;
    private boolean isOpen = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Window window = getWindow();
        NetWorkTools.taskbar_transparent(window, this);


        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_inside_search);
        //键盘弹起窗口可拉伸
        assistActivity(this);
        //获取控件
        tb_toolbar = (Toolbar) findViewById(R.id.video_icon);
        tv_title = (TextView) findViewById(R.id.video_title);
        tv_video = (TextView) findViewById(R.id.tv_video);
        tv_frequency = (TextView) findViewById(R.id.tv_frequency);
        tv_pdf = (TextView) findViewById(R.id.tv_pdf);
        tv_Image = (TextView) findViewById(R.id.tv_Image);
        wv_VideoDetails = (XWalkView) findViewById(R.id.wv_VideoDetails);
        scaleImageView = (ScaleImageView) findViewById(R.id.video_image);


        intent = getIntent();
        //设置标题、设置返回按钮并添加监听事件
        if (intent.getStringExtra("title") != null) {
            tv_title.setText(intent.getStringExtra("title"));
        } else {
            tv_title.setText("我的收藏");
        }



        isCircle = intent.getBooleanExtra("isCircle", false);
        tb_toolbar.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        readSharedHelper();
        //判断是否开启无图模式 然后显示Menu
        if (isOpen) {
            tb_toolbar.inflateMenu(R.menu.close_unmapped_mode_menu);
        } else {
            tb_toolbar.inflateMenu(R.menu.open_no_graph_mode_menu);
        }


        //给Toolbar的Menu设置点击事件
        tb_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    //点击开启无图模式
                    case R.id.open_no_graph_mode:
                        //清空Menu
                        tb_toolbar.getMenu().clear();
                        //重新给menu赋值
                        tb_toolbar.inflateMenu(R.menu.close_unmapped_mode_menu);
                        //设置isOpen的值
                        isOpen = true;
                        //webView重新加载页面
                        wv_VideoDetails.loadUrl(getPage(isCircle,isOpen,type));
                        Log.d("isOpen", isOpen + "");
                        break;
                    //点击关闭无图模式
                    case R.id.close_unmapped_mode:
                        //清空Menu
                        tb_toolbar.getMenu().clear();
                        //重新给menu赋值
                        tb_toolbar.inflateMenu(R.menu.open_no_graph_mode_menu);
                        //设置isOpen的值
                        isOpen = false;
                        //webView重新加载页面
                        wv_VideoDetails.loadUrl(getPage(isCircle,isOpen,type));
                        Log.d("isOpen", isOpen + "");
                        break;
                }
                return false;
            }
        });

        //默认选中视频  给视频、音频、文案、图片绑定单击事件
        tv_video.setSelected(true);
        tv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_video.setSelected(true);
                type = "avi";
                wv_VideoDetails.loadUrl(getPage(isCircle,isOpen,type));
                Log.v("url", getPage(isCircle,isOpen,type));
            }
        });
        tv_frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_frequency.setSelected(true);
                type = "mp3";
                wv_VideoDetails.loadUrl(getPage(isCircle,isOpen,type));
                Log.v("url", getPage(isCircle,isOpen,type));
            }
        });
        tv_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_pdf.setSelected(true);
                type = "pdf";
                wv_VideoDetails.loadUrl(getPage(isCircle,isOpen,type));
                Log.v("url", getPage(isCircle,isOpen,type));
            }
        });
        tv_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_Image.setSelected(true);
                type = "png";
                wv_VideoDetails.loadUrl(getPage(isCircle,isOpen,type));
                Log.v("url", getPage(isCircle,isOpen,type));
            }
        });

        //webView设置属性
        NetWorkTools.XWalkView_Settings(wv_VideoDetails,this);
        wv_VideoDetails.setUIClient(new XWalkUIClient(wv_VideoDetails){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                dialog(message);
                result.cancel();
                return true;
            }
        });
        wv_VideoDetails.loadUrl(getPage(isCircle,isOpen,type));
    }


    //得到当前按钮被选中的URL地址
    private String getPage(Boolean isCircle, Boolean isOpen, String type) {
        String page = "";
        if (isOpen) {
            switch (type) {
                case "avi":
                    page = MyApplication.url + "no_img_collect_video.view?type=avi";
                    break;
                case "mp3":
                    page = MyApplication.url + "no_img_collect_audio.view?type=mp3";
                    break;
                case "pdf":
                    page = MyApplication.url + "no_img_collect_courseware.view?type=pdf";
                    break;
                case "png":
                    page = MyApplication.url + "no_img_collect_picture.view?type=png";
                    break;
            }
        } else {
            switch (type) {
                case "avi":
                    page = MyApplication.url + "collect_video.view?type=avi";
                    break;
                case "mp3":
                    page = MyApplication.url + "collect_audio.view?type=mp3";
                    break;
                case "pdf":
                    page = MyApplication.url + "collect_courseware.view?type=pdf";
                    break;
                case "png":
                    page = MyApplication.url + "collect_picture.view?type=png";
                    break;
            }
        }
        if (isCircle) {
            page += "&w=a&cricleId=" + MyApplication.circleID;
        }
        return page;
    }


    //读取本地文件中无图模式是否开启
    public void readSharedHelper() {
        sharedHelper = new SharedHelper(getApplicationContext());
        Map<String, Object> data = sharedHelper.read();
        if (data.get("NoImages").toString().equals("true")) {
            isOpen = true;//开启
        } else {
            isOpen = false;//关闭
        }
    }

    //重置所有文本的选中状态
    private void setSelected() {
        tv_video.setSelected(false);
        tv_frequency.setSelected(false);
        tv_pdf.setSelected(false);
        tv_Image.setSelected(false);
    }

    protected void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyCollectionActivity.this, R.style.AlertDialog);
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
        if (type.equals("pdf")) {
            intent = new Intent("android.intent.Activity.PDFActivity");
            intent.putExtra("pdf", string);
            startActivity(intent);
        } else if (type.equals("avi")) {
            intent = new Intent("android.intent.Activity.CoursewareDetailsActivity");
            intent.putExtra("courseware", string);
            startActivity(intent);
        } else if (type.equals("mp3")) {
            intent = new Intent("android.intent.Activity.AudioDetailsActivity");
            intent.putExtra("audio", string);
            startActivity(intent);
        } else if (type.equals("png")) {
            Log.i("测试", "图片地址: " + string);
            if (string.contains("http")) {
                imgurl = string;
            } else {
                imgurl = MyApplication.url + "upload/" + string;
            }
            Log.i("测试", "图片地址: " + imgurl);
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
    public void isVIP() {
        dialogs();
    }

    protected void dialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyCollectionActivity.this, R.style.AlertDialog);
        builder.setMessage("您不是本圈子会员！请进行充值！");
        builder.setPositiveButton("充值", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent("android.intent.Activity.WeChat_Alipay_pay");
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

    //按返回键事件
    @Override
    public void onBackPressed() {
        int visibility = scaleImageView.getVisibility();
        /*View.VISIBLE 常量值为0，意思是可见的
        *View.INVISIBLE 常量值为4，意思是不可见的
        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
        * */
        if (visibility == 8) {
            finish();
        } else if (visibility == 0) {
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
        wv_VideoDetails.getNavigationHistory().clear();
        if (visibility == 8 && keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        } else if (visibility == 0 && keyCode == KeyEvent.KEYCODE_BACK) {
            scaleImageView.setVisibility(View.GONE);
        }
        return false;
    }
}
