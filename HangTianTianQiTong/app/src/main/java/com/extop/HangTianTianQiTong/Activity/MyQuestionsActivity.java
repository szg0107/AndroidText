package com.extop.HangTianTianQiTong.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;

/**
 * Created by Administrator on 2016/12/8.
 * 我的提问
 */

public class MyQuestionsActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "myQuestion.view?id=", reuse = "zero";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reuse = intent.getStringExtra("reuse");
        tw_title.setText(intent.getStringExtra("title"));
        toolbar.inflateMenu(R.menu.my_questions_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                intent = new Intent("android.intent.Activity.AskQuestions");
                intent.putExtra("reuse", reuse);
                startActivityForResult(intent,0);
                return false;
            }
        });
        xWalkView.loadUrl(url + MyApplication.circleID + "&reuse=" + reuse);//调用loadView方法为WebView加入链接
        Log.d("问题URL", url + MyApplication.circleID + "&reuse=" + reuse);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tw_search_box.setVisibility(View.VISIBLE);
        tw_search_box.setIconifiedByDefault(false);
        tw_search_box.setSubmitButtonEnabled(true);
        tw_search_box.setQueryHint("请输入搜索关键字");
        tw_search_box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 单击搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                intent = new Intent("android.intent.Activity.MyQuestionSsearchResults");
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
    }

    @JavascriptInterface
    public void setValue(String string) {
        intent = new Intent("android.intent.Activity.QuestionDetailsActivity");
        intent.putExtra("reuse", reuse);
        intent.putExtra("id", string);
        startActivity(intent);
    }

    @JavascriptInterface
    public void isVIP() {
        dialogs();
    }

    protected void dialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setMessage("您不是本圈子会员！请进行充值！");
        builder.setPositiveButton("充值", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.intent.Activity.WeChat_Alipay_pay");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    xWalkView.loadUrl(url + MyApplication.circleID + "&reuse=" + reuse);
                }
                break;
        }
    }
}
