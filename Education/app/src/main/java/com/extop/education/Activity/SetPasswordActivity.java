package com.extop.education.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xwalk.core.JavascriptInterface;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2016/12/7.
 * 设置密码
 */

public class SetPasswordActivity extends ToolbarWebViewActivity {
    String url = MyApplication.url + "setPassword.view?phoneNumber=";
    int i = 1;//记录页面调用原生方法的次数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("设置密码");
        xWalkView.loadUrl(url + intent.getStringExtra("phoneNumber"));//调用loadView方法为WebView加入链接
    }

    @JavascriptInterface
    public void setValue(String string) {
        if (i > 1) {
            //        intent = new Intent("android.intent.Activity.MainActivity");
            Log.d("注册回调", string);
//        startActivity(intent);
            MyApplication.userName = string;
            //获取融云token并连接融云服务器
            NetWorkTools.getRongCloudToken(string, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                        //连接融云服务器
                        RongIM.connect(json.getString("token"), new RongIMClient.ConnectCallback() {
                            @Override
                            public void onSuccess(String s) {
                                Log.d("成功连接融云", "--onSuccess--" + s);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                Log.e("融云错误", errorCode + "");
                            }

                            @Override
                            public void onTokenIncorrect() {

                            }
                        });
                        Log.d("返回的值", result);
                        Log.d("token", json.getString("token"));
                    } catch (Exception e) {
                        Log.e("PAY_GET", "异常：" + e.getMessage());
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("错误", ex + "");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
            startConversationList();
            finish();
        }
        i++;
    }

    //跳转会话列表界面
    private void startConversationList() {
        Map<String, Boolean> map = new HashMap<>();
        map.put(Conversation.ConversationType.PRIVATE.getName(), false); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
        map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示

        RongIM.getInstance().startConversationList(this, map);
    }
}
