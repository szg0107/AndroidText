package com.example.administrator.firstlineofcodetest.FilePersistenceTest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.firstlineofcodetest.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/*将数据存储到文件中与从文件中读取数据*/
public class FileOutputTestActivity extends AppCompatActivity {
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_output_test);
        //获取EditText的实例
        edit = (EditText) findViewById(R.id.edit);
        //读取文件中存储的文本内容
        String inputText = load();
        /**TextUtils.isEmpty()方法，可以一次性进行两种空值的判断。
        * 当传入的字符串等于null或者等于空字符串的时候,这个方法都会返回true，
         * 从而使得我们不需要先单独判断这两种空值再使用逻辑运算符连接起来。 */
        if (!TextUtils.isEmpty(inputText)) {
            //如果内容不为null，调用EditText的setText()方法将内容填充到EditText里
            edit.setText(inputText);
            //调用setSelection()方法将输入光标移动到文本的末尾位置以便于继续输入
            edit.setSelection(inputText.length());
            //弹出一句还原成功的提示
            Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //获取EditText的输入内容
        String inputText = edit.getText().toString();
        save(inputText);
    }

    //把输入的内容存储到文件中，文件名为data。
    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            //通过openFileOutput方法获得 FileOutputStream对象
            out = openFileOutput("data", Context.MODE_PRIVATE);
            //构建OutputStreamWriter对象并构建BufferedWriter对象
            writer = new BufferedWriter(new OutputStreamWriter(out));
            //通过BufferedWriter对象将文本内容写入到文件中
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //从文件中读取内容
    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            //通过openFileInput()获取FileInputStream对象
            in = openFileInput("data");
            //构建InputStreamReader对象并构建BufferedReader对象
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
