package com.example.administrator.firstlineofcodetest.FilePersistenceTest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.firstlineofcodetest.R;
//SharedPreferences储存练习
public class SharedPreferencesTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences_test);
        Button saveData = (Button) findViewById(R.id.save_data);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用SharedPreferences对象的edit()方法来获取SharedPreferences.Editor对象
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                //向SharedPreferences.Editor对象中添加数据
                editor.putString("name","Tom");
                editor.putInt("age",28);
                editor.putBoolean("married",false);
                //调用apply()方法将添加的数据提交,从而完成数据存储操作。
                editor.apply();
            }
        });
        Button restoreData= (Button) findViewById(R.id.restore_data);
        restoreData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
                String name=pref.getString("name","");
                int age=pref.getInt("age",0);
                boolean married=pref.getBoolean("married",false);
                Toast.makeText(getApplicationContext(), "name is" +name+",age is"+age+",married is"+married, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
