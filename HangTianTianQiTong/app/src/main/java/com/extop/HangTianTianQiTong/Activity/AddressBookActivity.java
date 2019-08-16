package com.extop.HangTianTianQiTong.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;

import org.xwalk.core.JavascriptInterface;

/**
 * Created by Administrator on 2017/7/31.
 * 通讯录
 */

public class AddressBookActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url+"addressbook.view?id=",groupCode="",title="a";
    private Boolean isGroup=true;
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tw_title.setText(title);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (intent.getStringExtra("groupCode").length()==0){
            groupCode="null";
            isGroup=false;
            tw_title.setText(intent.getStringExtra("title"));
            url=MyApplication.url+"circle_address_book.view?content=&id="+MyApplication.circleID;
            xWalkView.loadUrl(url);
            Log.d("圈子通讯录地址", url);
        }else {
            groupCode=intent.getStringExtra("groupCode");
            xWalkView.loadUrl(url+groupCode);//调用loadView方法为WebView加入链接
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("通讯录URL",url+"zuId"+groupCode);
        tw_search_box.setVisibility(View.VISIBLE);
        tw_search_box.setIconifiedByDefault(false);
        tw_search_box.setSubmitButtonEnabled(true);
        tw_search_box.setQueryHint("请输入搜索关键字");
        tw_search_box.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 单击搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isGroup){
                    intent = new Intent("android.intent.Activity.SearchAddressBookActivity");
                    intent.putExtra("content", query.trim());
                    intent.putExtra("groupCode",groupCode);
                    startActivity(intent);
                }else {
                    xWalkView.loadUrl("javascript:searchUser('" + query.trim() + "')");
                }

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
    //得到标题
    @JavascriptInterface
    public void getTitle(String string) {
        title=string;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message msg = new Message();
                msg.what = 0;
                handle.sendMessage(msg);
            }
        }).start();
    }
    //得到集团ID
    @JavascriptInterface
    public void getGroupCode(String string) {
        intent=new Intent("android.intent.Activity.AddressBookActivity");
        intent.putExtra("groupCode",string);
        startActivity(intent);
    }
    //得到用户手机号
    @JavascriptInterface
    public void getPhoneNumber(String string) {
        intent=new Intent("android.intent.Activity.EmployeeInformationActivity");
        intent.putExtra("phoneNumber",string);
        startActivity(intent);
    }

    //用户点返回键时直接关闭当前活动
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*View.VISIBLE 常量值为0，意思是可见的
        *View.INVISIBLE 常量值为4，意思是不可见的
        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
        * */
        xWalkView.getNavigationHistory().clear();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
}
