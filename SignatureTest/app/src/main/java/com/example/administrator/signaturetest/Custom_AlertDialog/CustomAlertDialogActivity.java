package com.example.administrator.signaturetest.Custom_AlertDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.administrator.signaturetest.R;
import com.example.administrator.signaturetest.SharedHelper;
import com.example.administrator.signaturetest.custom_Switch.Switch;

import java.util.Map;
import java.util.Random;

public class CustomAlertDialogActivity extends AppCompatActivity {

    private Button dialogOne, dialogTwo, dialogThree;
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private CustomAlertDialog customAlertDialog;
    String[] fruits = new String[]{"", "", "", ""};
    final String[] lesson = new String[]{"语文语文语文语文语文语文语文语文语文语文语文语文语文语文语文语文语文语文语文", "数学", "英语", "化学", "生物", "物理", "体育"};
    int ran, ran2, answerPosition, sum;
    String otherOptions;
    Boolean tag = true, isOpen = true;
    Switch iosSwitch;
    private SharedHelper sharedHelper;
    android.widget.Switch androidSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_alert_dialog_activity);
        mContext = CustomAlertDialogActivity.this;
        dialogOne = (Button) findViewById(R.id.dialogOne);
        dialogTwo = (Button) findViewById(R.id.dialogTwo);
        dialogThree= (Button) findViewById(R.id.dialogThree);

        //自定义对话框
        customAlertDialog = new CustomAlertDialog(this, R.style.MyDialog);
        customAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = customAlertDialog.getContent();
                customAlertDialog.clearContent();
                customAlertDialog.dismiss();
                Log.d("获得输入框的值", content);
            }
        });


        //仿ios的Switch
        iosSwitch = (Switch) findViewById(R.id.iosSwitch);
        iosSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (isOpen) {
                    iosSwitch.close();
                    isOpen = false;
                    editor.putBoolean("NoImages", false).commit();
                    Toast.makeText(getApplicationContext(), "关闭", Toast.LENGTH_SHORT).show();
                } else {
                    iosSwitch.open();
                    isOpen = true;
                    editor.putBoolean("NoImages", true).commit();
                    Toast.makeText(getApplicationContext(), "开启", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //原生自带Switch
        androidSwitch = (android.widget.Switch) findViewById(R.id.androidSwitch);
        androidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (buttonView.isChecked()) {
//                    sharedHelper.saves(true);
                    editor.putBoolean("NoImagesTwo", true).commit();
                    Toast.makeText(getApplicationContext(), "开关:ON", Toast.LENGTH_SHORT).show();
                } else {
//                    sharedHelper.saves(false);
                    editor.putBoolean("NoImagesTwo", false).commit();
                    Toast.makeText(getApplicationContext(), "开关:OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        readSharedHelper();

        //自定义dialog
        dialogOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.show();
            }
        });
        //带单选按钮列表的对话框
        dialogTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                // 返回一个0~(指定数-1)之间的随机值
                Random random = new Random();
                ran = random.nextInt(10);
                ran2 = random.nextInt(10);
                answerPosition = random.nextInt(4);
                sum = ran + ran2;
                fruits[answerPosition] = sum + "";
                for (int u = 0; u < fruits.length; ) {

                    if (u != answerPosition) {
                        otherOptions = random.nextInt(10) + sum + "";
                        tag = true;
                        for (String str : fruits) {
                            if (str.equals(otherOptions)) {
                                tag = false;
                                Log.d("数组中存在的数", otherOptions);
                                break;
                            } else {
                                Log.d("数组中没有的数", otherOptions);
                            }
                        }
                        if (tag) {
                            fruits[u] = otherOptions;
                            u++;
                        }
                    } else {
                        u++;
                    }
                }
                otherOptions = sum + "";
                alert = builder.setTitle(ran + "+" + ran2 + "=?" + answerPosition)
                        .setSingleChoiceItems(fruits, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "你选择了" + fruits[which], Toast.LENGTH_SHORT).show();
                                if (!fruits[which].equals(otherOptions)) {
                                    Toast.makeText(getApplicationContext(), "请选择正确答案", Toast.LENGTH_SHORT).show();
                                } else {
                                    alert.dismiss();
                                }

                            }
                        }).create();
                alert.show();
            }
        });
        //列表dialog
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_dialog_item,lesson);
        dialogThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert = null;
                builder = new AlertDialog.Builder(CustomAlertDialogActivity.this);
                /*alert = builder.setItems(lesson, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "你选择了" + lesson[which], Toast.LENGTH_SHORT).show();
                            }
                        }).create();
                alert.show();*/
                builder.setCancelable(false);
                alert=builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "你选择了" + lesson[i], Toast.LENGTH_SHORT).show();
                    }
                }).create();
                alert.show();
            }
        });

    }

    public void readSharedHelper() {
        sharedHelper = new SharedHelper(this);
        Map<String, Object> data = sharedHelper.read();
        if (data.get("NoImages").toString().equals("true")) {
            iosSwitch.open();
            isOpen = true;
        } else {
            iosSwitch.close();
            isOpen = false;
        }
        if (data.get("NoImagesTwo").toString().equals("true")) {
            androidSwitch.setChecked(true);
        } else {
            androidSwitch.setChecked(false);
        }
    }
}
