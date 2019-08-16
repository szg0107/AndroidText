package com.example.administrator.signaturetest.jian_ting_wang_luo_zhuang_tai_fu_wu;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.signaturetest.R;
import com.example.administrator.signaturetest.jian_ting_wang_luo_zhuang_tai_fu_wu.MyReceiver;

/**
 * Created by Administrator on 2017/5/27.
 * 调用网络状态服务
 */

public class ReceiveTestActivity extends AppCompatActivity implements MyReceiver.BRInteraction {
    MyReceiver myReceiver;
    Boolean isNet;
    int i=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        //核心部分代码：
        myReceiver = new MyReceiver();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, itFilter);
        myReceiver.setBRInteractionListener(this);
    }
    //别忘了将广播取消掉哦~
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @Override
    public void setText(Boolean content) {
        isNet=content;
        Toast.makeText(this,"是否有网"+isNet+i,Toast.LENGTH_SHORT).show();
        i++;
    }
}
