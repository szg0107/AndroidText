package com.szg.homemakingapplication.UI.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.szg.homemakingapplication.MyApplication;
import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.accesstool.AES;
import com.szg.homemakingapplication.accesstool.NetWorkTools;
import com.szg.homemakingapplication.accesstool.SharedHelper;
import com.szg.homemakingapplication.model.UserParentBean;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/17.
 */

public class LogIn extends AppCompatActivity {
    EditText etUserName,etUserPassWord;
    CheckBox ckRememberPwd,ckAutoLogin;
    private SharedHelper sharedHelper;
    private UserParentBean userParentBean=new UserParentBean();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        etUserName= (EditText) findViewById(R.id.user_names);
        etUserPassWord= (EditText) findViewById(R.id.user_password);
        Button btnlogin= (Button) findViewById(R.id.btn_login);
        ckRememberPwd= (CheckBox) findViewById(R.id.rememberpwd);
        ckRememberPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if(compoundButton.isChecked()){
                    editor.putBoolean("RememberPwd",true).commit();
                }else{
                    editor.putBoolean("RememberPwd",false).commit();
                }
            }
        });
        ckAutoLogin= (CheckBox) findViewById(R.id.autologin);
        ckAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if(compoundButton.isChecked()){
                    editor.putBoolean("AutoLogin",true).commit();
                }else{
                    editor.putBoolean("AutoLogin",false).commit();
                }
            }
        });
        sharedHelper=new SharedHelper(this);
        x.Ext.init(getApplication());
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetWorkTools.requestLog(etUserName.getText().toString(), AES.encode(etUserPassWord.getText().toString()), new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        userParentBean= JSON.parseObject(result,UserParentBean.class);
                        MyApplication.userBean=userParentBean.getData();
                        if(userParentBean.getMessage()==null){
                            Intent intent=new Intent();
                            intent.setAction("android.intent.szg.MainActivity");
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(x.app(),userParentBean.getMessage(),Toast.LENGTH_LONG).show();
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
                if(ckRememberPwd.isChecked()){
                    sharedHelper.save(etUserName.getText().toString(),AES.encode(etUserPassWord.getText().toString()),true);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Map<String,Object> data = sharedHelper.read();
        if (data.get("RememberPwd").toString().equals("true")){
            ckRememberPwd.setChecked(true);
            etUserName.setText(data.get("username").toString());
            etUserPassWord.setText(AES.decode(data.get("passwd").toString()));
        }else{
            ckRememberPwd.setChecked(false);
        }
        if (data.get("AutoLogin").toString().equals("true")){
            ckAutoLogin.setChecked(true);
            ckRememberPwd.setChecked(true);
            etUserName.setText(data.get("username").toString());
            etUserPassWord.setText(AES.decode(data.get("passwd").toString()));
            NetWorkTools.requestLog(data.get("username").toString(),data.get("passwd").toString(), new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    userParentBean= JSON.parseObject(result,UserParentBean.class);
                    MyApplication.userBean=userParentBean.getData();
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
            Intent intent=new Intent();
            intent.setAction("android.intent.szg.MainActivity");
            startActivity(intent);
            finish();
        }else{
            ckAutoLogin.setChecked(false);
        }
    }
}
