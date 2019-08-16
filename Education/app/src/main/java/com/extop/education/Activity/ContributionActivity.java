package com.extop.education.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
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
 * Created by Administrator on 2017/4/14.
 * 全部贡献和个人贡献
 */

public class ContributionActivity extends AppCompatActivity {
    private Toolbar tb_icon;
    private TextView tv_AllContributions,tv_MyContributions,tv_contribution_title;
    private XWalkView wv_Contribution;
    private String url = MyApplication.url+"allContribution.view?id=",reuse="",AllContributions="",MyContributions="";
    private SearchView sw_searChBox;
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
        tv_contribution_title= (TextView) findViewById(R.id.contribution_title);
        intent=getIntent();
        reuse=intent.getStringExtra("reuse");
        AllContributions=intent.getStringExtra("all");
        MyContributions=intent.getStringExtra("individual");

        //设置标题
        tv_contribution_title.setText(intent.getStringExtra("title"));
        tb_icon= (Toolbar) findViewById(R.id.contribution_icon);
        tb_icon.inflateMenu(R.menu.my_questions_menu);
        tb_icon.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                intent=new Intent("android.intent.Activity.PutContributionActivity");
                intent.putExtra("reuse",reuse);
                startActivity(intent);
                return false;
            }
        });
        //全部菜单按钮
        tv_AllContributions= (TextView) findViewById(R.id.tv_AllContributions);
        tv_AllContributions.setSelected(true);
        tv_AllContributions.setText(AllContributions);
        //个人贡献菜单按钮
        tv_MyContributions= (TextView) findViewById(R.id.tv_MyContributions);
        tv_MyContributions.setText(MyContributions);

        tb_icon.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_AllContributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_AllContributions.setSelected(true);
                url=MyApplication.url+"allContribution.view?id="+MyApplication.circleID+"&reuse="+reuse+"&content= ";
                Log.d("全部贡献URL",url);
                wv_Contribution.loadUrl(url);
            }
        });
        tv_MyContributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_MyContributions.setSelected(true);
                url=MyApplication.url+"myContribution.view?id="+MyApplication.circleID+"&reuse="+reuse;
                Log.d("个人贡献URL",url);
                wv_Contribution.loadUrl(url);
            }
        });


        wv_Contribution= (XWalkView) findViewById(R.id.wv_Contribution);
        tb_icon.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        NetWorkTools.XWalkView_Settings(wv_Contribution,this);
        sw_searChBox = (SearchView) findViewById(R.id.details_search_box);
        sw_searChBox.setIconifiedByDefault(false);
        sw_searChBox.setSubmitButtonEnabled(true);
        sw_searChBox.setQueryHint("请输入搜索关键字");
        sw_searChBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 单击搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
//                wv_MyGroup.loadUrl("javascript:search('"+query.trim()+"')");
                Intent intent = new Intent("android.intent.Activity.SearchContributionActivity");
                intent.putExtra("content", query.trim());
                intent.putExtra("reuse",reuse);
                intent.putExtra("AllContributions",AllContributions);
                intent.putExtra("MyContributions",MyContributions);
                startActivity(intent);
                //防止数据两次加载
                sw_searChBox.setIconified(true);
                return false;
            }

            // 用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        wv_Contribution.addJavascriptInterface(this,"callByJs");
        wv_Contribution.loadUrl(url+ MyApplication.circleID+"&reuse="+reuse+"&content= ");//调用loadView方法为WebView加入链接
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wv_Contribution != null) {
            wv_Contribution.pauseTimers();
            wv_Contribution.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wv_Contribution != null) {
            wv_Contribution.resumeTimers();
            wv_Contribution.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_Contribution != null) {
            wv_Contribution.onDestroy();
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
    public void setValue(String string) {
        Intent intent=new Intent("android.intent.Activity.ContributionDetailsActivity");
        intent.putExtra("id",string);
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
