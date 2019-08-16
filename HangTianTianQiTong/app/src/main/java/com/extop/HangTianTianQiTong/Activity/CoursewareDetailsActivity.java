package com.extop.HangTianTianQiTong.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/27.
 * 视频详情
 */

public class CoursewareDetailsActivity extends AppCompatActivity {
    private Intent intent;
    String url = MyApplication.url + "upload/";
    private ProgressDialog pd;
    Thread thread;
    VideoView videoView;
    NetworkStateReceivers receiver;//检测网络连接服务
    IntentFilter itFilter;//意图过滤器
    int count = 0;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            if (!videoView.isPlaying()) {
                Log.d("未播放", "11");
                handler.postDelayed(runnable, 20);//每0.5秒监听一次是否在播放视频
            } else {
                pd.cancel();
                handler.removeCallbacks(runnable);
                Log.d("停止线程", "22");
                if (MyApplication.videoYZ.equals("05")) {
                    Log.d("is_Virtual_key", MyApplication.videoYZ);
                    videoView.pause();
                    list_dialog();
                }
            }
        }
    };


    String[] fruits = new String[]{"", "", "", ""};//和的数组
    //ran随机数1 ran2随机数2 answerPosition正确答案位置 sum和
    int ran, ran2, answerPosition, sum;
    String otherOptions;//其他选项
    Boolean tag = true;//标记位
    private android.app.AlertDialog alert = null;//列表alert
    private String video_Name = "", video_Url = "", videoPath = "";
    private File file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //绑定布局
        setContentView(R.layout.courseware_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.video_icon);


        //开启服务核心部分代码：
//        receiver = new NetworkStateReceivers();
//        itFilter = new IntentFilter();
//        itFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");


        pd = new ProgressDialog(CoursewareDetailsActivity.this);
        intent = getIntent();
        //判断是互联探索服务器还是客户服务器
        video_Name = intent.getStringExtra("courseware");
        if (video_Name.contains("http")) {
            video_Url = video_Name;
        } else {
            video_Url = url + video_Name;
        }
        file = new File(Environment.getExternalStorageDirectory() + "/" + video_Name);
        videoPath = Environment.getExternalStorageDirectory() + "/" + video_Name;
        //调用系统自带的播放器
        videoView = (VideoView) this.findViewById(R.id.video_view);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        thread = new Thread(runnable);
        if (!file.exists()) {
            if (isWifiConnecteds(this)) {
//                registerReceiver(receiver, itFilter);
                downloadVideo(video_Url, video_Name);
            } else {
                dialog();
            }

        } else {
            videoView.setVideoPath(videoPath);
            thread.start();
            videoView.start();
        }
        //设置菜单并监听单击事件
        toolbar.inflateMenu(R.menu.file_transfer);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //由文件得到uri
                Uri videoUri = Uri.fromFile(new File(videoPath));
                Log.d("share", "uri:" + videoUri);  //输出：file:///storage/emulated/0/test.jpg

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
                shareIntent.setType("video/*");
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                return false;
            }
        });
        //设置返回按钮并添加监听事件
        toolbar.setNavigationIcon(R.mipmap.arrow_left_d);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        videoView.setVideoURI(uri);


//        videoView.requestFocus();
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CoursewareDetailsActivity.this, R.style.AlertDialog);
        builder.setMessage("您正在使用移动网络下载，继续下载将产生流量费用");
        builder.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                downloadVideo(video_Url, video_Name);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    protected void dialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CoursewareDetailsActivity.this, R.style.AlertDialog);
        builder.setMessage("您正在使用移动网络播放，继续播放将产生流量费用");
        builder.setPositiveButton("继续播放", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                videoView.start();
                count = 0;
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                unregisterReceiver(receiver);
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    public class NetworkStateReceivers extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("网络状态发生变化");
            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取ConnectivityManager对象对应的NetworkInfo对象
                //获取WIFI连接的信息
                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                //获取移动数据连接的信息
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//                    Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();

                } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
//                    Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();

                } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                    Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
                    videoView.pause();
                    if (count == 0) {
                        dialogs();
                        count = 1;
                    }
                } else {
                    Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();

                }
                //API大于23时使用下面的方式进行网络监听
            } else {

                System.out.println("API level 大于23");
                //获得ConnectivityManager对象
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                //获取所有网络连接的信息
                Network[] networks = connMgr.getAllNetworks();
                //用于存放网络连接信息
                StringBuilder sb = new StringBuilder();
                //通过循环将网络信息逐个取出来
                for (int i = 0; i < networks.length; i++) {
                    //获取ConnectivityManager对象对应的NetworkInfo对象
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                    sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
                }
//                Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
                Log.d("网络状态", sb.toString());

                if (sb.toString().equals("MOBILE connect is true")) {
                    //数据连接已开启
                    videoView.pause();
                    if (count == 0) {
                        dialogs();
                        count = 1;
                    }

                }
            }

        }
    }

    //列表dialog
    protected void list_dialog() {
        alert = null;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CoursewareDetailsActivity.this);
        // 返回一个0~(指定数-1)之间的随机值
        Random random = new Random();
        ran = random.nextInt(10);
        ran2 = random.nextInt(10);
        answerPosition = random.nextInt(4);
        sum = ran + ran2;
        fruits[answerPosition] = sum + "";
        //循环判断结果数组中的值是否重复
        for (int u = 0; u < fruits.length; ) {
            //u不等于正确位置
            if (u != answerPosition) {
                //其他位置等于0~9中任意一个数加上和
                otherOptions = random.nextInt(10) + sum + "";
                tag = true;
                for (String str : fruits) {
                    //如果其他位置的数在数组中没有就存入
                    if (str.equals(otherOptions)) {
                        tag = false;
                        Log.d("数组中存在的数", otherOptions);
                        break;
                    } else {
                        Log.d("数组中没有的数", otherOptions);
                    }
                }
                if (tag) {
                    fruits[u] = otherOptions;
                    u++;
                }
            } else {
                u++;
            }
        }
        otherOptions = sum + "";
        alert = builder.setTitle(ran + "+" + ran2 + "=?" + answerPosition)
                .setSingleChoiceItems(fruits, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "你选择了" + fruits[which], Toast.LENGTH_SHORT).show();
                        if (!fruits[which].equals(otherOptions)) {
                            Toast.makeText(getApplicationContext(), "请选择正确答案", Toast.LENGTH_SHORT).show();
                        } else {
                            videoView.start();
                            alert.dismiss();
                        }

                    }
                }).create();
        builder.setCancelable(false);
        alert.show();
    }

    //是否是WiFi连接
    public static boolean isWifiConnecteds(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (isWifiConnecteds(this)) {
//            unregisterReceiver(receiver);
//        }
    }


    //下载audio
    private void downloadVideo(String Video_Url, String Video_Name) {
        try {
            com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();
            http.download(Video_Url, Environment.getExternalStorageDirectory() + "/" + Video_Name, true, false, new RequestCallBack<File>() {
                @Override
                public void onStart() {
                }

                @Override
                public void onLoading(final long total, final long current, boolean isUploading) {

                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度条风格，风格为圆形，旋转的或者条形的
                    pd.setCancelable(true);// 设置是否可以通过点击Back键取消
                    pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                    pd.setTitle("资源加载中,请稍候...");// 设置ProgressDialog提示信息
                    int max = (int) total/1024;
                    int progress = (int) current/1024;
                    pd.setMax(max);// 设置进度条的最大值
                    pd.setProgress(progress);// 设置当前进度
                    pd.show();
                    super.onLoading(total, current, isUploading);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    pd.dismiss();
                    videoView.setVideoPath(videoPath);
                    thread.start();
                    videoView.start();
                    Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {

                }
            });
        } catch (Exception e) {
            Log.d("error", e.toString());
        }

    }
}
