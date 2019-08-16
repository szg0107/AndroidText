package com.example.administrator.signaturetest.MusicPlayer;


import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.administrator.signaturetest.R;

/**
 *
 * 在线播放mp3
 */
public class MusicPlayerActivity extends AppCompatActivity {
    private VideoView videoView;
    private LinearLayout controllerLayout;
    private ImageView play_controller_img;
    private TextView time_current_tv, time_total_tv;
    private SeekBar play_seek, volume_seek;
    public static final int UPDATE_UI = 1;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        audioManager= (AudioManager) getSystemService(AUDIO_SERVICE);
        ininUI();
        setPlayerEvent();
//        本地视频播放
//        videoView.setVideoPath("");
//        网络视频播放
        videoView.setVideoURI(Uri.parse("http://59.110.226.225/upload/1490943148245_1.mp3"));
        videoView.start();
        UIHandler.sendEmptyMessage(UPDATE_UI);
//        使用MediaController控制视频播放
/*        MediaController controller=new MediaController(this);
//        设置VideoView与MediaController建立关联
        videoView.setMediaController(controller);
//        设置MediaController与VideoView建立关联
        controller.setMediaPlayer(videoView);*/
    }

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
                    updateTextViewWithTimeFormat(time_current_tv,progress);
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
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
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
    private void ininUI() {
        videoView = (VideoView) findViewById(R.id.videoView);
        controllerLayout = (LinearLayout) findViewById(R.id.controllerbar_layout);
        play_controller_img = (ImageView) findViewById(R.id.pause_img);
        time_current_tv = (TextView) findViewById(R.id.time_current_tv);
        time_total_tv = (TextView) findViewById(R.id.time_total_tv);
        play_seek = (SeekBar) findViewById(R.id.play_seek);
        volume_seek = (SeekBar) findViewById(R.id.volume_seek);
//        获取当前设备的最大音量
        int streamMaxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        获取当前的音量
        int streamVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume_seek.setMax(streamMaxVolume);
        volume_seek.setProgress(streamVolume);
    }

}
