package com.example.administrator.firstlineofcodetest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/*功能菜单列表*/
public class MainActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //自定义图片
        getView(R.id.myImage,"android.intent.Activity.MyImageActivity");
        //activity生命周期练习
        getView(R.id.activityLifecycle,"android.intent.Activity.ActivityLifecycle");
        //Fragment生命周期练习
        getView(R.id.fragmentText,"android.intent.Activity.FragmentTextActivity");
        //fragment实践   简易版的新闻应用
        getView(R.id.newsShow,"android.intent.Activity.NewsShowActivity");
        //广播练习之监听网络状态
        getView(R.id.broadcastTest,"android.intent.Activity.BroadcastTestActivity");
        //广播实践 实现强制下线功能
        getView(R.id.forceOffline,"android.intent.Activity.LoginActivity");
        //文件储存 将数据存储到文件中与从文件中读取数据
        getView(R.id.fileOutputTest,"android.intent.Activity.FileOutputTestActivity");
        //SharedPreferences储存练习
        getView(R.id.SharedPreferencesTest,"android.intent.Activity.SharedPreferencesTestActivity");
        //SQLite数据库储存练习
        getView(R.id.MyDatabaseTest,"android.intent.Activity.MyDatabaseTest");
    }
    private void getView(int id, final String action) {
        View mView = findViewById(id);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(action);
//                传递参数
//                intent.putExtra("phoneNumber",string);
                startActivity(intent);
            }
        });

    }
}
