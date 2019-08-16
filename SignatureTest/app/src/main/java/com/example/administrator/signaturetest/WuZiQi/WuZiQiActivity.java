package com.example.administrator.signaturetest.WuZiQi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.administrator.signaturetest.R;

public class WuZiQiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wu_zi_qi);
        WuZiQiPanel wuziqiPanel= (WuZiQiPanel) findViewById(R.id.wuZiQi);
    }
}
