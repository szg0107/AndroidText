package com.extop.education.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import com.extop.education.Adapter.SharedHelper;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;
import com.extop.education.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkView;

import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/6.
 * 圈子详情
 */

public class CircleDetailsActivity extends ToolbarWebViewActivity {
    String url = MyApplication.url + "CircleDetails.view?id=", groupCode = "", admin = "";
    //    name修改后菜单的名称，order修改后的顺序，number内容的数量 urls我的应用的地址
    String[] name, order, number, urls, isOpenCategory, name_one, order_one, number_one, isOpenCategory_one;
    //个人贡献，贡献1，我要回答，回答1,二级菜单名称
    String[] personal_contribution, personal_contribution_one, answer, answer_one;
    //读取本地文件内容
    private SharedHelper sharedHelper;
    //无图模式是否开启
    private boolean isJGG = true;
    //时间戳
    long timestamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将Activity添加到Activity列表中方便移除
        MyApplication.addActivity(this);

        //设置标题
        tw_title.setText(MyApplication.circleName);

        toolbar.inflateMenu(R.menu.cancel_attention_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
/*//              构建了一个 Intent，只不过这个 Intent 仅仅是用于传递数据而已，它没有指定任何的“意图”。
                Intent intent = new Intent();
                intent.putExtra("circleid", MyApplication.circleID);
//              setResult()方法是专门用于向上一个活动返回数据的。
//              setResult() 方法接收两个参数，第一个参数用于向上一个活动返回处理结果，一般只使用 RESULT_OK 或 RESULT_CANCELED 这两个值，第二个参数则是把带有数据的 Intent 传递回去，然后调用 了 finish () 方法来销毁当前活动。
                setResult(RESULT_OK, intent);
                finish();*/
                if (groupCode == "") {
                    dialog();
                } else {
                    dialogs();
                }
                return false;
            }
        });

//        webView.addJavascriptInterface(this, "callByJs");
        //获取当前时间戳
        Date d = new Date();
        timestamp = d.getTime();
        //本地文件读取
        readSharedHelper();
        if (intent.getStringExtra("admin").length() == 0) {
            admin = "null";
        } else {
            admin = intent.getStringExtra("admin");
        }
        //圈子是否认证
        if (MyApplication.circleRZ.equals("01")) {
            //用户没有实名认证进入提示页，否则进入圈子详情
            if (!MyApplication.userRZ.equals("04")) {
                url = MyApplication.url + "authenticationFailure.view";
                xWalkView.loadUrl(url);
            } else {
//                wv_CircleDetails.loadUrl(url + MyApplication.circleID + "&admin=" + intent.getStringExtra("admin") + "&time=" + timestamp+"&isJGG=true");
                //加载不同的页面
                webViewLoadURL();
            }
        } else {
//            wv_CircleDetails.loadUrl(url + MyApplication.circleID + "&admin=" + intent.getStringExtra("admin") + "&time=" + timestamp+"&isJGG=true");
            webViewLoadURL();
        }
        readSharedHelper();
    }

    //非集团应用退出提示
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CircleDetailsActivity.this, R.style.AlertDialog);
        builder.setMessage("确认取消关注吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
//              构建了一个 Intent，只不过这个 Intent 仅仅是用于传递数据而已，它没有指定任何的“意图”。
//                Intent intent = new Intent();
//                intent.putExtra("circleid", MyApplication.circleID);
//              setResult()方法是专门用于向上一个活动返回数据的。
//              setResult() 方法接收两个参数，第一个参数用于向上一个活动返回处理结果，一般只使用 RESULT_OK 或 RESULT_CANCELED 这两个值，第二个参数则是把带有数据的 Intent 传递回去，然后调用 了 finish () 方法来销毁当前活动。
//                setResult(RESULT_OK, intent);
                xWalkView.loadUrl("javascript:deleteAttention('" + MyApplication.circleID + "')");
                setResult(RESULT_OK);
                dialog.dismiss();
                finish();
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

    //集团应用退出提示
    protected void dialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CircleDetailsActivity.this, R.style.AlertDialog);
        builder.setMessage("本工作圈为集团应用工作圈，不能退出!");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    //js可以调用的方法 菜单项响应的点击事件
    @JavascriptInterface
    public void setValue(String serialNumber, String listName,String url) {
        Log.d("serialNumber",serialNumber);
        Log.d("listName",listName);
        Log.d("url",url);
        //1.序号 2.列名 3.拓展功能url
        switch (serialNumber) {
            case "0":
                //必修
                intent = new Intent("android.intent.Activity.VideoDetailsActivity");
                //标题名称
                intent.putExtra("title", listName);
                //二级菜单名称
                intent.putExtra("name", name);
                //二级菜单排列顺序
                intent.putExtra("order", order);
                //二级菜单所包含的内容数量
                intent.putExtra("number", number);
                //是否开启类目
                intent.putExtra("isOpenCategory", isOpenCategory);
                intent.putExtra("reuse", "zero");
                startActivity(intent);
                break;
            case "1":
                //贡献
                intent = new Intent("android.intent.Activity.ContributionActivity");
                //标题名称
                intent.putExtra("title", listName);
                //重复使用
                intent.putExtra("reuse", "zero");
                //"全部"菜单的名称
                intent.putExtra("all", personal_contribution[3]);
                //"个人贡献"菜单的名称
                intent.putExtra("individual", personal_contribution[4]);
                startActivity(intent);
                break;
            case "2":
                //我要提问
                intent = new Intent("android.intent.Activity.MyQuestionsActivity");
                intent.putExtra("title", listName);
                intent.putExtra("reuse", "zero");
                startActivity(intent);
                break;
            case "3":
                //我要回答
                intent = new Intent("android.intent.Activity.AnswerActivity");
                intent.putExtra("title", listName);
                intent.putExtra("reuse", "zero");
                //最新的问题的标题
                intent.putExtra("NewQuestion", answer[3]);
                //历史回答的标题
                intent.putExtra("HistoricalAnswer", answer[4]);
                startActivity(intent);
                break;
            case "4":
                //我要考试
                intent = new Intent("android.intent.Activity.ExaminationActivity");
                intent.putExtra("title", listName);
                startActivity(intent);
                break;
            case "5":
                //我的网络业绩
                intent = new Intent("android.intent.Activity.MyNetworkPerformanceActivity");
                intent.putExtra("title", listName);
                startActivity(intent);
                break;
            case "6":
                //我的综合评价
                intent = new Intent("android.intent.Activity.MyComprehensiveEvaluationActivity");
                intent.putExtra("title", listName);
                startActivity(intent);
                break;
            case "7":
                //圈子通知
                intent = new Intent("android.intent.Activity.CircleNotificationActivity");
                intent.putExtra("title", listName);
                intent.putExtra("reuse", "zero");
                startActivity(intent);
                break;
            case "8":
                //圈子动态
                intent = new Intent("android.intent.Activity.CircleDynamicActivity");
                intent.putExtra("title", listName);
                intent.putExtra("reuse", "zero");
                startActivity(intent);
                break;
            case "9":
                //我的任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","");
                startActivity(intent);
                break;
            case "10":
                //我的收藏
                intent = new Intent("android.intent.Activity.MyCollectionActivity");
                intent.putExtra("title", listName);
                intent.putExtra("isCircle", true);
                startActivity(intent);
                break;
            case "11":
                //缴费记录
                intent = new Intent("android.intent.Activity.InternalPaymentRecordsActivity");
                intent.putExtra("title", listName);
                startActivity(intent);
                break;
            case "12":
                //我的应用1   根据URL地址不同调用不同的Activity处理
                intent = new Intent("android.intent.Activity.MyController");
                intent.putExtra("title", listName);
                intent.putExtra("url", urls[0]);
                startActivity(intent);
                break;
            case "13":
                //我的应用2
                intent = new Intent("android.intent.Activity.MyController");
                intent.putExtra("title", listName);
                intent.putExtra("url", urls[1]);
                startActivity(intent);
                break;
            case "14":
                //我的应用3
                intent = new Intent("android.intent.Activity.MyController");
                intent.putExtra("title", listName);
                intent.putExtra("url", urls[2]);
                startActivity(intent);
                break;
            case "15":
                //我的应用4
                intent = new Intent("android.intent.Activity.MyController");
                intent.putExtra("title", listName);
                intent.putExtra("url", urls[3]);
                startActivity(intent);
                break;
            case "16":
                //我的应用5
                intent = new Intent("android.intent.Activity.MyController");
                intent.putExtra("title", listName);
                intent.putExtra("url", urls[4]);
                startActivity(intent);
                break;
            case "17":
                //我的应用6
                intent = new Intent("android.intent.Activity.MyController");
                intent.putExtra("title", listName);
                intent.putExtra("url", urls[5]);
                startActivity(intent);
                break;
            case "18":
                //我的应用7
                intent = new Intent("android.intent.Activity.MyController");
                intent.putExtra("title", listName);
                intent.putExtra("url", urls[6]);
                startActivity(intent);
                break;
            case "19":
                //公司必修2
                intent = new Intent("android.intent.Activity.VideoDetailsActivity");
                intent.putExtra("title", listName);
                intent.putExtra("name", name_one);
                intent.putExtra("order", order_one);
                intent.putExtra("number", number_one);
                //是否开启类目
                intent.putExtra("isOpenCategory", isOpenCategory_one);
                intent.putExtra("reuse", "one");
                startActivity(intent);
                break;
            case "20":
                //个人贡献2
                intent = new Intent("android.intent.Activity.ContributionActivity");
                intent.putExtra("title", listName);
                intent.putExtra("reuse", "one");
                //"全部"菜单的名称
                intent.putExtra("all", personal_contribution_one[3]);
                //"个人贡献"菜单的名称
                intent.putExtra("individual", personal_contribution_one[4]);
                startActivity(intent);
                break;
            case "21":
                //我要提问2
                intent = new Intent("android.intent.Activity.MyQuestionsActivity");
                intent.putExtra("title", listName);
                intent.putExtra("reuse", "one");
                startActivity(intent);
                break;
            case "22":
                //我要回答2
                intent = new Intent("android.intent.Activity.AnswerActivity");
                intent.putExtra("title", listName);
                intent.putExtra("reuse", "one");
                //最新的问题的标题
                intent.putExtra("NewQuestion", answer_one[3]);
                //历史回答的标题
                intent.putExtra("HistoricalAnswer", answer_one[4]);
                startActivity(intent);
                break;
            case "23":
                //圈子通知2
                intent = new Intent("android.intent.Activity.CircleNotificationActivity");
                intent.putExtra("title", listName);
                intent.putExtra("reuse", "one");
                startActivity(intent);
                break;
            case "24":
                //圈子动态2
                intent = new Intent("android.intent.Activity.CircleDynamicActivity");
                intent.putExtra("title", listName);
                intent.putExtra("reuse", "one");
                startActivity(intent);
                break;
            case "25":
                //通讯录
                intent = new Intent("android.intent.Activity.AddressBookActivity");
                intent.putExtra("title", listName);
                intent.putExtra("groupCode", groupCode);
                startActivity(intent);
                break;
            case "积分排名":
                intent = new Intent("android.intent.Activity.LeaderBoardActivity");
                intent.putExtra("title", listName);
                startActivity(intent);
                break;
            case "电子商务":
                intent = new Intent("android.intent.Activity.ProductListActivity");
                intent.putExtra("title", listName);
                startActivity(intent);
                break;
            case "上传资料":
                intent = new Intent("android.intent.Activity.UploadDataActivity");
                intent.putExtra("title", listName);
                startActivity(intent);
                break;
            case "选择类任务":
                //选择类任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","15");
                startActivity(intent);
                break;
            case "缴费类任务":
                //缴费类任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","16");
                startActivity(intent);
                break;
            case "图文类任务":
                //图文类任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","17");
                startActivity(intent);
                break;
            case "考试类任务":
                //考试类任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","23");
                startActivity(intent);
                break;
            case "统计类任务":
                //统计类任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","24");
                startActivity(intent);
                break;
            case "问卷类任务":
                //问卷类任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","25");
                startActivity(intent);
                break;
            case "审核类任务":
                //审核类任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","26");
                startActivity(intent);
                break;
            case "差异通知类任务":
                //差异通知类任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","27");
                startActivity(intent);
                break;
            case "汇签类任务":
                //汇签类任务
                intent = new Intent("android.intent.Activity.MyTaskActivity");
                intent.putExtra("title", listName);
                intent.putExtra("type","28");
                startActivity(intent);
                break;
            case "999":
                //申请成为圈子管理员
                intent = new Intent("android.intent.Activity.CircleAdministratorActivity");
                intent.putExtra("title", listName);
                startActivity(intent);
                break;


        }
    }

    //得到视频、文案、音频、图片的排序顺序、修改后的名称、位置、内容数量的数组
    @JavascriptInterface
    public void getArray(String[] name, String[] order, String[] number, String[] urls, String[] isOpenCategory) {
        //1.修改后菜单的名称，2修改后的顺序，3内容的数量
        this.name = name;
        this.order = order;
        this.number = number;
        this.urls = urls;
        this.isOpenCategory = isOpenCategory;
//        outputArray(this.name);
//        outputArray(this.order);
//        outputArray(this.numbe);
//        outputArray(this.isOpenCategory);
    }

    //得到视频、文案、音频、图片的排序顺序、修改后的名称、位置、内容数量的数组
    @JavascriptInterface
    public void getArray_one(String[] name, String[] order, String[] number, String[] isOpenCategory_one) {
        //1.修改后菜单的名称，2修改后的顺序，3内容的数量
        this.name_one = name;
        this.order_one = order;
        this.number_one = number;
        this.isOpenCategory_one = isOpenCategory_one;
//        outputArray(this.isOpenCategory_one);
    }

    //得到集团代码
    @JavascriptInterface
    public void getGroupCode(String groupCode) {
        this.groupCode = groupCode;
        Log.d("groupCode", this.groupCode);
    }

    //得到贡献、贡献1、回答、回答1子菜单的名称
    @JavascriptInterface
    public void getSubMenuName(String[] personal_contribution, String[] personal_contribution_one, String[] answer, String[] answer_one) {
        this.personal_contribution = personal_contribution;
        this.personal_contribution_one = personal_contribution_one;
        this.answer = answer;
        this.answer_one = answer_one;
    }

    //输出数组
    private void outputArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            Log.d("数组的值", array[i]);
        }
        Log.d("分割线", "----------------------------");
    }

    //读取本地文件中无图模式是否开启
    public void readSharedHelper() {
        sharedHelper = new SharedHelper(getApplicationContext());
        Map<String, Object> data = sharedHelper.read();
        if (data.get("isCircleDetailsJGG").toString().equals("true")) {
            isJGG = true;
        } else {
            isJGG = false;
        }
    }

    //webView加载页面
    public void webViewLoadURL() {
        if (isJGG) {
            xWalkView.loadUrl(url + MyApplication.circleID + "&admin=" + admin + "&time=" + timestamp + "&isJGG=" + isJGG);
            Log.d("圈子详情地址", url + MyApplication.circleID + "&admin=" + admin + "&time=" + timestamp + "&isJGG=true");
        } else {
            xWalkView.loadUrl(url + MyApplication.circleID + "&admin=" + admin + "&time=" + timestamp);
            Log.d("圈子详情地址", url + MyApplication.circleID + "&admin=" + admin + "&time=" + timestamp);
        }
    }
}
