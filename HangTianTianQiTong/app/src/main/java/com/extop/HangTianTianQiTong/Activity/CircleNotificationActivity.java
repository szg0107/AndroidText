package com.extop.HangTianTianQiTong.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * Created by Administrator on 2017/6/30.
 * 圈子通知
 */

public class CircleNotificationActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url+"circle_notification.view?id=";
    private String reuse="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reuse=intent.getStringExtra("reuse");
        tw_title.setText(intent.getStringExtra("title"));

        xWalkView.setUIClient(new XWalkUIClient(xWalkView){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                dialog(message);
                result.cancel();
                return true;
            }
        });




        tw_search_box.setVisibility(View.VISIBLE);
        tw_search_box.setIconifiedByDefault(false);
        tw_search_box.setSubmitButtonEnabled(true);
        tw_search_box.setQueryHint("请输入搜索关键字");
        tw_search_box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 单击搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                intent = new Intent("android.intent.Activity.SearchCircleNotificationActivity");
                intent.putExtra("content", query.trim());
                intent.putExtra("reuse", reuse);
                xWalkView.loadUrl("javascript:SearchRecords(" + MyApplication.circleID + ",'" + query.trim() + "')");
                startActivity(intent);
                //防止数据两次加载
                tw_search_box.setIconified(true);
                return false;
            }

            // 用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        xWalkView.loadUrl(url+ MyApplication.circleID+"&type=inform&reuse="+reuse);//调用loadView方法为WebView加入链接
        Log.d("url", url+ MyApplication.circleID+"&type=inform&reuse="+reuse);
    }
    //详情弹窗
    protected void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CircleNotificationActivity.this,R.style.AlertDialog);
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
        intent = new Intent("android.intent.Activity.InformTheDetailsActivity");
        intent.putExtra("id", string);
        startActivity(intent);
    }
}
