package com.extop.HangTianTianQiTong.Activity;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import com.extop.HangTianTianQiTong.Adapter.SharedHelper;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/30.
 * 圈子动态
 */

public class CircleDynamicActivity extends ToolbarWebViewActivity {
    private SharedHelper sharedHelper;
    private boolean isOpen = true;
    private String reuse="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        reuse=intent.getStringExtra("reuse");
        tw_title.setText(intent.getStringExtra("title"));

        tw_search_box.setVisibility(View.VISIBLE);
        tw_search_box.setIconifiedByDefault(false);
        tw_search_box.setSubmitButtonEnabled(true);
        tw_search_box.setQueryHint("请输入搜索关键字");
        tw_search_box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 单击搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                intent = new Intent("android.intent.Activity.SearchCircleDynamicActivity");
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
//        readSharedHelper();
        //判断是否开启无图模式 然后显示Menu
        /*if (isOpen) {
            tb_icon.inflateMenu(R.menu.close_unmapped_mode_menu);
        } else {
            tb_icon.inflateMenu(R.menu.open_no_graph_mode_menu);
        }*/

        //给Toolbar的Menu设置点击事件
        /*tb_icon.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    //点击开启无图模式
                    case R.id.open_no_graph_mode:
                        //清空Menu
                        tb_icon.getMenu().clear();
                        //重新给menu赋值
                        tb_icon.inflateMenu(R.menu.close_unmapped_mode_menu);
                        //设置isOpen的值
                        isOpen = true;
                        //webview重新加载页面
                        webView.loadUrl(getPage(isOpen));
                        Log.d("isOpen", isOpen + "");
                        break;
                    //点击关闭无图模式
                    case R.id.close_unmapped_mode:
                        //清空Menu
                        tb_icon.getMenu().clear();
                        //重新给menu赋值
                        tb_icon.inflateMenu(R.menu.open_no_graph_mode_menu);
                        //设置isOpen的值
                        isOpen = false;
                        //webview重新加载页面
                        webView.loadUrl(getPage(isOpen));
                        Log.d("isOpen", isOpen + "");
                        break;
                }
                return false;
            }
        });*/
        //处理页面alert事件
        xWalkView.setUIClient(new XWalkUIClient(xWalkView){
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                dialog(message);
                result.cancel();
                return true;
            }
        });
        //加载页面
        xWalkView.loadUrl(getPage(isOpen));//调用loadView方法为WebView加入链接
        Log.d("url",getPage(isOpen));
    }
    //响应页面调用原生事件
    @JavascriptInterface
    public void setValue(String string) {
        Intent intent=new Intent("android.intent.Activity.PDFActivity");
        intent.putExtra("pdf",string);
        startActivity(intent);

    }

    //详情弹窗
    protected void dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CircleDynamicActivity.this,R.style.AlertDialog);
        builder.setMessage(message);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    //得到当前按钮被选中的URL地址
    private String getPage(Boolean isOpen) {
        String page = "";
        if (isOpen) {
            page=MyApplication.url+"no_imgcircle_dynamic.view?id="+ MyApplication.circleID+"&type=dynamic&reuse="+reuse;
        } else {
            page=MyApplication.url+"circle_dynamic.view?id="+ MyApplication.circleID+"&type=dynamic&reuse="+reuse;
        }
        return page;
    }

    //读取本地文件中无图模式是否开启
    public void readSharedHelper() {
        sharedHelper = new SharedHelper(getApplicationContext());
        Map<String, Object> data = sharedHelper.read();
        if (data.get("NoImages").toString().equals("true")) {
            isOpen = true;//开启
//            Log.d("isOpen", isOpen+"");
        } else {
            isOpen = false;//关闭
//            Log.d("isOpen", isOpen+"");
        }
    }
}
