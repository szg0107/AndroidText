package com.szg.homemakingapplication.UI.PersonalCenterActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.UI.Login.LogIn;
import com.szg.homemakingapplication.accesstool.AES;
import com.szg.homemakingapplication.accesstool.NetWorkTools;
import com.szg.homemakingapplication.accesstool.SharedHelper;
import com.szg.homemakingapplication.model.Bean;
import com.szg.homemakingapplication.support.adapter.fragment.PersonalCenterFragment;

import org.xutils.common.Callback;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/20.
 */

public class Change_the_password extends AppCompatActivity {
    private EditText Old_Password, New_Password, Retype_New_Password;
    private Button submit;
    private SharedHelper sharedHelper;
    private String oldpass, ss;
    private Map<String, Object> data;
    private Bean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_password_layout);
        Toolbar tbgoback= (Toolbar) findViewById(R.id.toolbar_change);
        tbgoback.setNavigationIcon(R.drawable.arrow_left_d);
        tbgoback.setTitle("修改密码");
        //点导航栏图标退出
        tbgoback.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sharedHelper = new SharedHelper(this);
        Old_Password = (EditText) findViewById(R.id.Old_Password_change);
        New_Password = (EditText) findViewById(R.id.New_Password_change);
        Retype_New_Password = (EditText) findViewById(R.id.Retype_New_Password_change);
        submit = (Button) findViewById(R.id.submit_change);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bean=new Bean();
        data = sharedHelper.read();
        Old_Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    oldpass = Old_Password.getText().toString();
                    ss = AES.decode(data.get("passwd").toString());
                    if (!oldpass.equals(ss)) {
                        Toast.makeText(Change_the_password.this, "请输入正确的原密码！", Toast.LENGTH_SHORT).show();
                        Old_Password.setText("");
                    }
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String News = New_Password.getText().toString();
                String retype = Retype_New_Password.getText().toString();
                if (!retype.equals(News)) {
                    Toast.makeText(Change_the_password.this, "两次密码输入不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                    Retype_New_Password.setSelectAllOnFocus(true);
                    Retype_New_Password.setText("");
                }
                String oldpwd = AES.encode(Old_Password.getText().toString());
                String  pwd= AES.encode(Retype_New_Password.getText().toString());
                NetWorkTools.requestChange_the_Password(pwd, oldpwd, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                         bean= JSON.parseObject(result, Bean.class);
                        if (bean.getCode().equals("200")) {
                            Toast.makeText(Change_the_password.this, "成功修改密码！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Change_the_password.this, LogIn.class);
                            SharedPreferences sp = getApplicationContext().getSharedPreferences("mysp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("RememberPwd", false).commit();
                            editor.putBoolean("AutoLogin", false).commit();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });

            }
        });
    }
}
