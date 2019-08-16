package com.extop.HangTianTianQiTong.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.extop.HangTianTianQiTong.Adapter.NetWorkTools;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkView;
import static com.extop.HangTianTianQiTong.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/7/31.
 * 搜索通讯录
 */

public class SearchAddressBookActivity extends AppCompatActivity {
    private Toolbar tb_icon;
    private TextView tv_NewQuestio,tv_HistoricalAnswer,tv_answer_title;
    private XWalkView wv_Answer;
    private String url = MyApplication.url+"addressbook_search_staff.view?code=",type="staff",id="",content="";
    private Intent intent;
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
        tb_icon= (Toolbar) findViewById(R.id.contribution_icon);
        tv_NewQuestio= (TextView) findViewById(R.id.tv_AllContributions);
        tv_NewQuestio.setSelected(true);
        tv_NewQuestio.setText("人员");
        tv_HistoricalAnswer= (TextView) findViewById(R.id.tv_MyContributions);
        tv_HistoricalAnswer.setText("公司");
        tv_answer_title= (TextView) findViewById(R.id.contribution_title);
        intent=getIntent();
        id=intent.getStringExtra("groupCode");
        content=intent.getStringExtra("content");
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
        wv_Answer.loadUrl(url+id+"&content="+content);//调用loadView方法为WebView加入链接
        Log.d("url",url+id+"&content="+content);
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

        tv_NewQuestio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                type="staff";
                tv_NewQuestio.setSelected(true);
                url=MyApplication.url+"addressbook_search_staff.view?code="+id+"&content="+content;
                wv_Answer.loadUrl(url);
                Log.d("人员", url);
            }
        });
        tv_HistoricalAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                type="company";
                tv_HistoricalAnswer.setSelected(true);
                url=MyApplication.url+"addressbook_search_company.view?code="+id+"&content="+content;
                wv_Answer.loadUrl(url);
                Log.d("公司", url);
            }
        });
    }
    //重置所有文本的选中状态
    private void setSelected() {
        tv_NewQuestio.setSelected(false);
        tv_HistoricalAnswer.setSelected(false);
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

    @JavascriptInterface
    public void setValue(String string) {
        switch (type){
            case "staff":
                intent=new Intent("android.intent.Activity.EmployeeInformationActivity");
                intent.putExtra("phoneNumber",string);
                startActivity(intent);
                break;
            case "company":
                intent=new Intent("android.intent.Activity.AddressBookActivity");
                intent.putExtra("groupCode",string);
                startActivity(intent);
                break;
        }
    }
}
