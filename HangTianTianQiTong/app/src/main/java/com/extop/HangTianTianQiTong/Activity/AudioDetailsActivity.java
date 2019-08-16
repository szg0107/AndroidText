package com.extop.HangTianTianQiTong.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.extop.HangTianTianQiTong.Adapter.NetWorkTools;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.xutils.common.Callback;

import java.io.File;

import static com.extop.HangTianTianQiTong.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/4/1.
 * 音频详情
 */

public class AudioDetailsActivity extends AppCompatActivity {
    private Toolbar tb_icon;//标题
    private Intent intent;
    private String url = MyApplication.url + "upload/";
    private VideoView videoView;
    private LinearLayout controllerLayout;
    private ImageView play_controller_img,audio_img;//播放控制
    private TextView time_current_tv, time_total_tv, audio_title_tv;//播放时间和总播放时间
    private SeekBar play_seek, volume_seek;//音量和播放进度
    public static final int UPDATE_UI = 1;
    private AudioManager audioManager;//音频管理器
    private String Audio_Name="",Audio_Url="",audioPath="";
    private ProgressDialog dialog;//下载进度
    private File file;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改状态栏颜色
        Window window = getWindow();
        NetWorkTools.taskbar_transparent(window, this);
        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //绑定布局
        setContentView(R.layout.audio_details);
        //键盘弹起窗口可拉伸
        assistActivity(this);
        //获取控件
        tb_icon = (Toolbar) findViewById(R.id.audio_icon);
        tb_icon.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_icon.inflateMenu(R.menu.file_transfer);
        tb_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //设置标题字体为微软雅黑
        audio_title_tv = (TextView) findViewById(R.id.audio_title);
        intent = getIntent();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audio_img= (ImageView) findViewById(R.id.audio_img);
        initUI();
        setPlayerEvent();
        dialog = new ProgressDialog(this);
//        网络视频播放
        Audio_Name = intent.getStringExtra("audio");
        if (Audio_Name.contains("http")) {
            Audio_Url = Audio_Name;
        } else {
            Audio_Url = url + Audio_Name;
        }
        //播放网络视频
//        Uri uri = Uri.parse(url+intent.getStringExtra("audio"));
//        videoView.setVideoURI(uri);
        file = new File(Environment.getExternalStorageDirectory() +"/"+ Audio_Name);
        audioPath=Environment.getExternalStorageDirectory() + "/"+Audio_Name;

        tb_icon.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //由文件得到uri
                Uri audioUri = Uri.fromFile(new File(audioPath));
                Log.d("share", "uri:" + audioUri);  //输出：file:///storage/emulated/0/test.jpg

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, audioUri);
                shareIntent.setType("audio/*");
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                return false;
            }
        });
        if(!file.exists()){
            downloadAudio(Audio_Url,Audio_Name);
        }else {
            videoView.setVideoPath(audioPath);
            videoView.start();
            UIHandler.sendEmptyMessage(UPDATE_UI);
        }




        NetWorkTools.getAudioImage(Audio_Name, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(final String result) {
//                audio_img.setImageBitmap(NetWorkTools.getURLImage(MyApplication.url + "upload/" + NetWorkTools.replaceBlank(result)));
                Log.d("图片地址", MyApplication.url + "upload/" + NetWorkTools.replaceBlank(result) + "");
//                Log.d("bitmap",NetWorkTools.getURLImage(MyApplication.url + "upload/" + NetWorkTools.replaceBlank(result))+"");
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                    Bitmap bmp = NetWorkTools.getURLImage(MyApplication.url + "upload/" + NetWorkTools.replaceBlank(result));
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = bmp;
                        System.out.println("000");
                        handle.sendMessage(msg);
                    }
                }).start();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("错误", ex + "");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //在消息队列中实现对控件的更改
    @SuppressLint("HandlerLeak")
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    audio_img.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };
    private void updateTextViewWithTimeFormat(TextView textView, int millisecond) {
        int second = millisecond / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String str = null;
        if (hh != 0) {
            str = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            str = String.format("%02d:%02d", mm, ss);
        }
        textView.setText(str);
    }

    private Handler UIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_UI) {
//           获取视频当前的播放时间
                int currentPosition = videoView.getCurrentPosition();
//           获取视频播放的总时间
                int totalduration = videoView.getDuration();
//           格式视频播放时间
                updateTextViewWithTimeFormat(time_current_tv, currentPosition);
                updateTextViewWithTimeFormat(time_total_tv, totalduration);
                play_seek.setMax(totalduration);
                play_seek.setProgress(currentPosition);
                UIHandler.sendEmptyMessageDelayed(UPDATE_UI, 500);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        UIHandler.removeMessages(UPDATE_UI);
    }

    //          控制视频的播放或暂停
    private void setPlayerEvent() {
        play_controller_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    play_controller_img.setImageResource(R.drawable.media_play);
//                    暂停播放
                    videoView.pause();
                    UIHandler.removeMessages(UPDATE_UI);
                } else {
                    play_controller_img.setImageResource(R.drawable.media_pause);
//                    继续播放
                    videoView.start();
                    UIHandler.sendEmptyMessage(UPDATE_UI);
                }
            }
        });
        play_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTextViewWithTimeFormat(time_current_tv, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                UIHandler.removeMessages(UPDATE_UI);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
//              令视频播放进度遵循seekbar停止拖动的这一刻进度
                videoView.seekTo(progress);
                UIHandler.sendEmptyMessage(UPDATE_UI);
            }
        });
        volume_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                设置当前设备的音量
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    //        初始化UI布局
    private void initUI() {
        videoView = (VideoView) findViewById(R.id.audio_view);
        controllerLayout = (LinearLayout) findViewById(R.id.controllerbar_layout);
        play_controller_img = (ImageView) findViewById(R.id.pause_img);
        time_current_tv = (TextView) findViewById(R.id.time_current_tv);
        time_total_tv = (TextView) findViewById(R.id.time_total_tv);
        play_seek = (SeekBar) findViewById(R.id.play_seek);
        volume_seek = (SeekBar) findViewById(R.id.volume_seek);
//        获取当前设备的最大音量
        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        获取当前的音量
        int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume_seek.setMax(streamMaxVolume);
        volume_seek.setProgress(streamVolume);
    }

    //下载audio
    private void downloadAudio(String Audio_Url,String Audio_Name) {
        try {
            com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();
            http.download(Audio_Url, Environment.getExternalStorageDirectory() + "/"+Audio_Name, true, false, new RequestCallBack<File>() {
                @Override
                public void onStart() {
                }

                @Override
                public void onLoading(final long total, final long current, boolean isUploading) {

                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
                    dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                    dialog.setTitle("资源加载中,请稍候...");
                    int max = (int) total/1024;
                    int progress = (int) current/1024;
                    dialog.setMax(max);// 设置进度条的最大值
                    dialog.setProgress(progress);// 设置当前进度
                    dialog.show();
                    super.onLoading(total, current, isUploading);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    dialog.dismiss();
                    videoView.setVideoPath(audioPath);
                    videoView.start();
                    UIHandler.sendEmptyMessage(UPDATE_UI);
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
