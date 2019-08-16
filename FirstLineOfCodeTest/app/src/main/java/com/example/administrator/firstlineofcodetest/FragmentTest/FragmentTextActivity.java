package com.example.administrator.firstlineofcodetest.FragmentTest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.firstlineofcodetest.R;

public class FragmentTextActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_text);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        //创建待添加的碎片实例
        replaceFragment(new RightFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                Log.d("点击", "按钮");
                replaceFragment(new RightFragment());
                break;
        }
    }
    //动态添加RightFragment碎片
    private void replaceFragment(Fragment fragment) {
        //获取FragmentManager，在活动中可以直接通过调用getSupportFragmentManager()方法得到。
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启一个事务，通过调用beginTransaction()方法开启。
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //向容器内添加或替换碎片，一般使用replace()方法实现，需要传入容器的id和待添加的碎片实例。
        transaction.replace(R.id.right_layout, fragment);
        //在碎片中模拟返回栈
        transaction.addToBackStack(null);
        //提交事务,调用commit()方法来完成。
        transaction.commit();
    }
}
