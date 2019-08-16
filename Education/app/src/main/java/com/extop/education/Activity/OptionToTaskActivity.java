package com.extop.education.Activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;
import com.extop.education.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * Created by Administrator on 2017/7/12.
 * 选项任务
 */

public class OptionToTaskActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url+"option_to_task.view?taskId=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("任务详情");

        //获取当前时间戳
//        Date d = new Date();
//        long timestamp=d.getTime();
//        Log.d("时间戳",timestamp+"");
        xWalkView.setUIClient(new XWalkUIClient(xWalkView){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                NetWorkTools.dialog(OptionToTaskActivity.this,message);
                result.cancel();
                return true;
            }
        });
        xWalkView.loadUrl(url+intent.getStringExtra("taskID")+"&selectNumber="+intent.getStringExtra("selectNumber"));//调用loadView方法为WebView加入链接
        Log.d("url",url+intent.getStringExtra("taskID")+"&selectNumber="+intent.getStringExtra("selectNumber"));
    }
    @JavascriptInterface
    public void setValue(String string) {
        finish();
//        dialog(this,string);
    }
    //消息对话框
    public void dialog(AppCompatActivity activity, String[] message) {
        String str="";
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialog);
        for (int i=0;i<message.length;i++){
            str+=message[i]+"\n";
        }
        builder.setMessage(str);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
}
