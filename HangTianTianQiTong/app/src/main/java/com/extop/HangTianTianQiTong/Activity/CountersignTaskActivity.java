package com.extop.HangTianTianQiTong.Activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.extop.HangTianTianQiTong.Adapter.NetWorkTools;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;


/**
 * Created by szg on 2018/3/6.
 * 汇签任务
 */

public class CountersignTaskActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "countersignDetails.view?&taskId=", taskID = "", taskType = "";
    Intent intent;
    //缩放图片所需的变量
    private String imgUrl = "";
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
        tw_title.setText("任务详情");
        //获取当前时间戳
//        Date d = new Date();
//        long timestamp=d.getTime();
//        Log.d("时间戳",timestamp+"");
        intent = getIntent();
        taskID = intent.getStringExtra("taskID");
        taskType=intent.getStringExtra("taskType");
        MyApplication.addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //如果任务类型不为空，说明是已完成任务，要访问已完成地址
        if (taskType!=null){
            url=MyApplication.url + "ywc_hqrw.view?taskId=";
        }
        xWalkView.loadUrl(url + taskID);//调用loadView方法为WebView加入链接
        Log.d("url", url + taskID);
    }

    //附件点击事件
    @JavascriptInterface
    public void setValue(String string, String type) {
        if (type.equals("pdf")) {
            intent = new Intent("android.intent.Activity.PDFActivity");
            intent.putExtra("pdf", string);
            startActivity(intent);
        } else {
            if (string.contains("http")) {
                imgUrl = string;
            } else {
                imgUrl = MyApplication.url + "upload/" + string;
            }
            loadingPictures(imgUrl);
        }

    }

    //菜单点击事件
    @JavascriptInterface
    public void setMessage(String[] string) {
        /*0.菜单的值 1.传递方式 2.任务创建人*/
        switch (string[0]) {
            case "签署意见":
                intent = new Intent("android.intent.Activity.CommentsContractsActivity");
                intent.putExtra("taskID", taskID);
                //传递方式或人员类型
                intent.putExtra("transmittalMode", string[1]);
                Log.d("传递方式",string[1]);
                startActivity(intent);
                break;
            case "终止":
                dialogs(string[0]);
                break;
            case "退还":
                dialogs(string[0]);
                break;
            case "传递领导":
                intent = new Intent("android.intent.Activity.TransmitActivity");
                intent.putExtra("taskID", taskID);
                //传递方式或人员类型
                intent.putExtra("transmittalModeValue", string[0]);
                startActivity(intent);
                break;
            case "传递员工":
                intent = new Intent("android.intent.Activity.TransmitActivity");
                intent.putExtra("taskID", taskID);
                //传递方式或人员类型
                intent.putExtra("transmittalModeValue", string[0]);
                startActivity(intent);
                break;
            case "文件状态":
                intent = new Intent("android.intent.Activity.FileStatusActivity");
                intent.putExtra("taskID", taskID);
                //任务创建人
                intent.putExtra("taskFounder", string[2]);
                startActivity(intent);
                break;
            case "流程追踪":
                intent = new Intent("android.intent.Activity.TracingFlowActivity");
                intent.putExtra("taskID", taskID);
                startActivity(intent);
                break;
            case "关闭":
                finish();
                break;
        }
    }

    //显示对话框并加载图片
    public void loadingPictures(String imgUrls) {
        //缩放图片的方法
        imgUrl = imgUrls;
        pd = new ProgressDialog(CountersignTaskActivity.this);
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
                Bitmap bmp = NetWorkTools.getURLImage(imgUrl);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();
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
        xWalkView.getNavigationHistory().clear();
        if (visibility == 8 && keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        } else if (visibility == 0 && keyCode == KeyEvent.KEYCODE_BACK) {
            scaleImageView.setVisibility(View.GONE);
        }
        return false;
    }

    //对话框
    protected void dialogs(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CountersignTaskActivity.this, R.style.AlertDialog);
        builder.setMessage("您确定要"+message+"吗?");
//        final String cz=message;
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                xWalkView.post(new Runnable() {
                    @Override
                    public void run() {
                        xWalkView.loadUrl("javascript:flowRecord('" + message + "')");
                    }
                });
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
}
