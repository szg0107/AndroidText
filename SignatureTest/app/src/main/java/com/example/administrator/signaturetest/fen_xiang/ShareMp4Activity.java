package com.example.administrator.signaturetest.fen_xiang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.administrator.signaturetest.R;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * Created by Administrator on 2017/9/11.
 */

public class ShareMp4Activity extends AppCompatActivity {
    ProgressDialog dialog;
    String Apk_Url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        dialog = new ProgressDialog(this);
        Apk_Url="http://123.56.187.157/hlts/upload/1510396822606_1.mp4";
        File file = new File(Environment.getExternalStorageDirectory() + "/sp.mp4");
        Log.d("aaa",Environment.getExternalStorageDirectory() + File.separator + "sp.mp4");
        Log.d("bbb",Environment.getExternalStorageDirectory() + "/sp.mp4");
        if (!file.exists()) {
            Log.d("aaa",file.exists()+"1");

            downloadAPK();
        }
    }
    /* 创建菜单 */
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,0,0,"分享");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                // intent.setType("text/plain"); //纯文本
            /*
             * 图片分享 it.setType("image/png"); 　//添加图片 File f = new
             * File(Environment.getExternalStorageDirectory()+"/name.png");
             *
             * Uri uri = Uri.fromFile(f); intent.putExtra(Intent.EXTRA_STREAM,
             * uri); 　
             */
                /*Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));*/

                String imagePath = Environment.getExternalStorageDirectory() + "/sp.mp4";
                //由文件得到uri
                Uri imageUri = Uri.fromFile(new File(imagePath));
                Log.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/test.jpg

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("video/*");
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                return true;
        }
        return false;
    }



    //下载APK
    private void downloadAPK() {
        try {
            com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();
            http.download(Apk_Url, Environment.getExternalStorageDirectory() + "/sp.mp4", true, false, new RequestCallBack<File>() {
                @Override
                public void onStart() {
                }

                @Override
                public void onLoading(final long total, final long current, boolean isUploading) {

                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
                    dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                    dialog.setTitle("视频正在下载请稍等……");
                    int max = (int) total;
                    int progress = (int) current;
                    dialog.setMax(max);// 设置进度条的最大值
                    dialog.setProgress(progress);// 设置当前进度
                    dialog.show();
                    super.onLoading(total, current, isUploading);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    File file = new File(Environment.getExternalStorageDirectory() + "/sp.mp4");
                    dialog.dismiss();
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
