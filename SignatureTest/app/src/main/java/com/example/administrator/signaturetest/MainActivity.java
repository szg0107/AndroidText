package com.example.administrator.signaturetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //自定义dialog
        attachEvent(R.id.custom_alert_dialog,"android.intent.Activity.CustomAlertDialogActivity");
        //分享文本
        attachEvent(R.id.share,"android.intent.Activity.ShareActivity");

        //下载音频并分享
        attachEvent(R.id.shareAudio,"android.intent.Activity.ShareMp4Activity");
        //自定义音乐播放器
        attachEvent(R.id.musicPlayer,"android.intent.Activity.MusicPlayerActivity");
        //自定义五子棋
        attachEvent(R.id.wuZiQi,"android.intent.Activity.WuZiQiActivity");
        //自定义图片控件
        attachEvent(R.id.myImage,"android.intent.Activity.MyImageActivity");
        //自定义图片控件2
        attachEvent(R.id.scaleImageView,"android.intent.Activity.ScaleImageViewActivity");
        //Fresco和PhotoDraweeView实现加载大图与图片缩放
        attachEvent(R.id.photoDrawView,"android.intent.Activity.PhotoDraweeViewTextActivity");
        //加载网络图片
        attachEvent(R.id.loadNetworkImage,"android.intent.Activity.LoadNetworkImage");
        //上拉加载更多下拉刷新控件
        attachEvent(R.id.PullToRefreshAndLoad,"android.intent.Activity.PullToRefreshAndLoadActivity");
        //悬浮窗
        attachEvent(R.id.popupWindow,"android.intent.Activity.PopupWindowActivity");
        //检测底部虚拟键是否存在
        attachEvent(R.id.virtualButton,"android.intent.Activity.There_is_a_virtual_button");
        //监听网络服务
        attachEvent(R.id.receiveTest,"android.intent.Activity.ReceiveTestActivity");
        //在线阅读PDF
        attachEvent(R.id.showPDF,"android.intent.Activity.Download_the_PDF_and_display");
        //将PDF下载到本地阅读方式一
        attachEvent(R.id.downloadPDF,"android.intent.Activity.Download_the_PDF_and_display2");
        //将PDF下载到本地阅读方式二
        attachEvent(R.id.downloadPDF2,"android.intent.Activity.Download_the_PDF_and_display3");
        //将PDF下载到本地阅读方式三
        attachEvent(R.id.downloadPDF3,"android.intent.Activity.Download_the_PDF_and_display4");
        //分组列表
        attachEvent(R.id.groupList,"android.intent.Activity.GroupListActivity");
        //自动更新+启动动画
        attachEvent(R.id.displayPicturesDrag,"android.intent.Activity.Display_pictures_drag");
        //系统定位练习
        attachEvent(R.id.nativePositioning,"android.intent.Activity.NativePositioningActivity");
        //高德web服务+定位+反向地理编码
        attachEvent(R.id.showLocation,"android.intent.Activity.ShowLocation");
    }
    //绑定事件
    protected void attachEvent(int id, final String action){
        //获得控件
        Button mView=(Button)findViewById(id);
        //绑定点击事件
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转页面
                intent = new Intent(action);
                startActivity(intent);
            }
        });
    }
}
