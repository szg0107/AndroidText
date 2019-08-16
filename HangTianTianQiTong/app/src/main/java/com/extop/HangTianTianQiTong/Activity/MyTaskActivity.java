package com.extop.HangTianTianQiTong.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.util.Date;

import static com.extop.HangTianTianQiTong.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/7/11.
 * 我的任务
 */

public class MyTaskActivity extends AppCompatActivity {
    private Toolbar tb_icon;
    private XWalkView webView;
    private String url = MyApplication.url + "my_task.view?id=",type="";
    private TextView tv_AllContributions,tv_MyContributions;
    Intent intent;
    long timestamp;
    private SearchView sw_searChBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改状态栏颜色
        Window window = getWindow();
        NetWorkTools.taskbar_transparent(window, this);


        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //绑定布局
        setContentView(R.layout.contribution);
        //键盘弹起窗口可拉伸
        assistActivity(this);
        //获取控件
        TextView tv_questions_title = (TextView) findViewById(R.id.contribution_title);
        intent = getIntent();
        tv_questions_title.setText(intent.getStringExtra("title"));
        type=intent.getStringExtra("type");
        Log.d("type",type);
        sw_searChBox= (SearchView) findViewById(R.id.details_search_box);
        sw_searChBox.setVisibility(View.GONE);
        tv_AllContributions= (TextView) findViewById(R.id.tv_AllContributions);
        tv_AllContributions.setSelected(true);
        tv_AllContributions.setText("未完成");
        tv_MyContributions= (TextView) findViewById(R.id.tv_MyContributions);
        tv_MyContributions.setText("已完成");

        tb_icon = (Toolbar) findViewById(R.id.contribution_icon);
        tb_icon.setNavigationIcon(R.mipmap.arrow_left_d);
        webView = (XWalkView) findViewById(R.id.wv_Contribution);
        tb_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        NetWorkTools.XWalkView_Settings(webView, this);
        //获取当前时间戳
        Date d = new Date();
        timestamp = d.getTime();
        Log.d("时间戳", timestamp + "");
        webView.setUIClient(new XWalkUIClient(webView){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                NetWorkTools.dialog(MyTaskActivity.this, message);
                result.cancel();
                return true;
            }
        });
        webView.loadUrl(url + MyApplication.circleID + "&time=" + timestamp+"&type="+type);//调用loadView方法为WebView加入链接
        Log.d("my_task_url", url + MyApplication.circleID + "&time=" + timestamp+"&type="+type);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(url.contains("my_task")){
            url=MyApplication.url + "my_task.view?id=" + MyApplication.circleID + "&time=" + timestamp+"&type="+type;
            Log.d("未完成任务", url.contains("my_task")+"");
            webView.loadUrl(url);
        }
        tv_AllContributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_AllContributions.setSelected(true);
                url=MyApplication.url + "my_task.view?id=" + MyApplication.circleID + "&time=" + timestamp+"&type="+type;
                Log.d("未完成任务地址", url);
                webView.loadUrl(url);
            }
        });
        tv_MyContributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_MyContributions.setSelected(true);
                url=MyApplication.url + "ywcrw.view?id=" + MyApplication.circleID +"&type="+type;
                Log.d("已完成任务地址", url);
                webView.loadUrl(url);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.pauseTimers();
            webView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.resumeTimers();
            webView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.onDestroy();
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
        tv_AllContributions.setSelected(false);
        tv_MyContributions.setSelected(false);
    }

    @JavascriptInterface
    public void setValue(String[] string) {
        /*类型        id       选择数量     缴费类型      缴费金额     个性缴费金额
        * taskType, taskID, selectNumber,paymentType,paymentAmount,individualContribution*/
        for (int i = 0; i < string.length; i++) {
            Log.d(i + "", "值:" + string[i]);
        }
        switch (string[0]) {
            case "15":
                //选择类
                intent = new Intent("android.intent.Activity.OptionToTaskActivity");
                intent.putExtra("taskID", string[1]);
                intent.putExtra("selectNumber", string[2]);
                startActivity(intent);
                break;
            case "16":
                //缴费类
                intent = new Intent("android.intent.Activity.CaptureExpendsTaskActivity");
                intent.putExtra("taskID", string[1]);
                //根据缴费类型分情况传入金额
                switch (string[3]) {
                    case "18":
                        //统一    任务表缴费金额
                        intent.putExtra("paymentAmount", string[4]);
                        break;
                    case "19":
                        //自愿    手动输入缴费金额
                        intent.putExtra("paymentAmount", "null");
                        break;
                    case "20":
                        //个性    人员表缴费金额
                        intent.putExtra("paymentAmount", string[5]);
                        break;
                }
                startActivity(intent);
                break;
            case "17":
                //图文类 跳到问题详情 回答问题算完成任务 任务id等于问题ID
                intent = new Intent("android.intent.Activity.QuestionDetailsActivity");
                intent.putExtra("id", string[1]);
                intent.putExtra("reuse", "task");
                intent.putExtra("isTask", true);
                startActivity(intent);
                break;
            case "23":
                //考试类
                intent = new Intent("android.intent.Activity.ExaminationTaskActivity");
                intent.putExtra("taskID", string[1]);
                startActivity(intent);
                break;
            case "24":
                //统计类
                intent = new Intent("android.intent.Activity.StatisticsTaskActivity");
                intent.putExtra("taskID", string[1]);
                startActivity(intent);
                break;
            case "25":
                //问卷类
                intent = new Intent("android.intent.Activity.QuestionnaireTaskActivity");
                intent.putExtra("taskID", string[1]);
                startActivity(intent);
                break;
            case "26":
                //审核类
                intent = new Intent("android.intent.Activity.AuditTaskActivity");
                intent.putExtra("taskID", string[1]);
                startActivity(intent);
                break;
            case "27":
                //差异通知类
                intent = new Intent("android.intent.Activity.DifferentialNotificationTaskActivity");
                intent.putExtra("taskID", string[1]);
                startActivity(intent);
                break;
            case "28":
                //汇签类
                intent = new Intent("android.intent.Activity.CountersignTaskActivity");
                intent.putExtra("taskID", string[1]);
                startActivity(intent);
        }
    }
    /*已完成任务详情*/
    @JavascriptInterface
    public void getValue(String[] string) {
        /*类型        id
        * taskType, taskID*/
        for (int i = 0; i < string.length; i++) {
            Log.d(i + "", "值:" + string[i]);
        }
        if(string[0].equals("28")){
            intent = new Intent("android.intent.Activity.CountersignTaskActivity");
        }else {
            intent = new Intent("android.intent.Activity.CompleteTaskDetailsActivity");
        }
        intent.putExtra("taskType", string[0]);
        intent.putExtra("taskID", string[1]);
        startActivity(intent);
    }
}
