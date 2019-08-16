package com.extop.education.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.Adapter.SharedHelper;
import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;
import com.extop.education.R;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2017/4/27.
 * 欢迎界面
 */

public class WelComeActivity extends ToolbarWebViewActivity {
    String url = MyApplication.url + "WeLcome.view", userName = "", passWord = "";//webview访问的地址

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        MyApplication.addActivity(this);

        //获取控件并绑定监听事件
        toolbar.setVisibility(View.GONE);
        scaleImageView.setVisibility(View.VISIBLE);


        //根据地址类别显示动画的背景
        if (MyApplication.addressType == 2) {
            scaleImageView.setBackground(getResources().getDrawable(R.drawable.animations_tqt));
        } else {
            scaleImageView.setBackground(getResources().getDrawable(R.drawable.animations));
        }


        xWalkView.setResourceClient(new XWalkResourceClient(xWalkView) {
            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);
                scaleImageView.setVisibility(View.GONE);
                if (userName != "" && userName != null) {
                    xWalkView.loadUrl("javascript:AutoLogin('" + userName + "','" + passWord + "')");
                }
            }
        });
        xWalkView.setUIClient(new XWalkUIClient(xWalkView) {
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                Log.e("session", message);
                result.cancel();
                return true;
            }
        });
        xWalkView.loadUrl(url);
        //检测读写权限方法
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //需要弹出dialog让用户手动赋予权限
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedHelper sharedHelper = new SharedHelper(WelComeActivity.this);
        Map<String, Object> data = sharedHelper.read();
        userName = data.get("userName").toString();
        passWord = data.get("passWord").toString();
        Log.d("userName", data.get("userName").toString());
        Log.d("passWord", data.get("passWord").toString());
    }

    //js可以调用的方法
    @JavascriptInterface
    public void setValue(String string) {
        if (string.equals("succeed")) {
            MyApplication.userName = userName;
            //获取融云token并连接融云服务器
            NetWorkTools.getRongCloudToken(userName, new Callback.CommonCallback<String>() {
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
        } else {
            Intent intent = new Intent("android.intent.Activity.LoginActivity");
            startActivity(intent);
        }
        Log.d("欢迎的值", string);
    }

    //跳转会话列表界面
    private void startConversationList() {
        Map<String, Boolean> map = new HashMap<>();
        map.put(Conversation.ConversationType.PRIVATE.getName(), false); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
        map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示

        RongIM.getInstance().startConversationList(this, map);
    }

    //js可以调用的方法 得到搜索框是否显示
    @JavascriptInterface
    public void getSearch(String string) {
        MyApplication.isOpenSearch = string;
    }

    //检测读写权限方法
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用write

            } else {
                //用户不同意，向用户展示该权限作用
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("需要赋予访问存储的权限，不开启将无法正常工作！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                    return;
                }
                finish();
            }
        }
    }
}
