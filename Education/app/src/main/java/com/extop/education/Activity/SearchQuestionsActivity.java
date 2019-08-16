package com.extop.education.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.MyApplication;
import com.extop.education.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkView;

import static com.extop.education.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/4/25.
 * 最新问题与历史回答搜索页面
 */

public class SearchQuestionsActivity extends AppCompatActivity {
    private Toolbar tb_icon;
    private TextView tv_NewQuestion,tv_HistoricalAnswer,tv_answer_title;
    private XWalkView wv_Answer;
    private String url = MyApplication.url+"newQuestion.view?id=",reuse="",content="";
    Intent intent;
    private SearchView sw_searChBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改状态栏颜色
        Window window=getWindow();
        NetWorkTools.taskbar_transparent(window,this);

        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //绑定布局
        setContentView(R.layout.contribution);
        //键盘弹起窗口可拉伸
        assistActivity(this);
        //获取控件
        intent=getIntent();
        reuse=intent.getStringExtra("reuse");
        content=intent.getStringExtra("content");
        sw_searChBox= (SearchView) findViewById(R.id.details_search_box);
        sw_searChBox.setVisibility(View.GONE);
        tb_icon= (Toolbar) findViewById(R.id.contribution_icon);
        tv_NewQuestion= (TextView) findViewById(R.id.tv_AllContributions);
        tv_NewQuestion.setText(intent.getStringExtra("NewQuestion"));
        tv_NewQuestion.setSelected(true);

        tv_HistoricalAnswer= (TextView) findViewById(R.id.tv_MyContributions);
        tv_HistoricalAnswer.setText(intent.getStringExtra("HistoricalAnswer"));
        tv_answer_title= (TextView) findViewById(R.id.contribution_title);
        tv_answer_title.setText("搜索结果");

        wv_Answer= (XWalkView) findViewById(R.id.wv_Contribution);
        tb_icon.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        NetWorkTools.XWalkView_Settings(wv_Answer,this);
        wv_Answer.loadUrl(url+ MyApplication.circleID+"&content="+content+"&reuse="+reuse);//调用loadView方法为WebView加入链接
    }

    @Override
    protected void onStart() {
        super.onStart();
        tb_icon.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_NewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_NewQuestion.setSelected(true);
                url=MyApplication.url+"newQuestion.view?id="+MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                wv_Answer.loadUrl(url);
            }
        });
        tv_HistoricalAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_HistoricalAnswer.setSelected(true);
                url=MyApplication.url+"searchAnsweredQuestion.view?id="+MyApplication.circleID+"&content="+content+"&reuse="+reuse;
                wv_Answer.loadUrl(url);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wv_Answer != null) {
            wv_Answer.pauseTimers();
            wv_Answer.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wv_Answer != null) {
            wv_Answer.resumeTimers();
            wv_Answer.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_Answer != null) {
            wv_Answer.onDestroy();
        }
    }

    //用户点返回键时直接关闭当前活动
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
    //重置所有文本的选中状态
    private void setSelected() {
        tv_NewQuestion.setSelected(false);
        tv_HistoricalAnswer.setSelected(false);
    }

    @JavascriptInterface
    public void setValue(String string) {
        Intent intent=new Intent("android.intent.Activity.QuestionDetailsActivity");
        intent.putExtra("id",string);
        intent.putExtra("reuse",reuse);
        startActivity(intent);
    }

    @JavascriptInterface
    public void isVIP(){
        dialogs();
    }

    protected void  dialogs(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialog);
        builder.setMessage("您不是本圈子会员！请进行充值！");
        builder.setPositiveButton("充值", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent("android.intent.Activity.WeChat_Alipay_pay");
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
}
