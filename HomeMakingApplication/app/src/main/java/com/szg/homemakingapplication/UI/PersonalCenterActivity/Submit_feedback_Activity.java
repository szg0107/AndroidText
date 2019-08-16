package com.szg.homemakingapplication.UI.PersonalCenterActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.accesstool.NetWorkTools;
import com.szg.homemakingapplication.model.Bean;

import org.xutils.common.Callback;

/**
 * Created by Administrator on 2016/9/20.
 */

public class Submit_feedback_Activity extends AppCompatActivity {
    EditText comments,tell;
    Button submit;
    private Bean bean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.feedback_layout);
        bean=new Bean();
        Toolbar tbgoback= (Toolbar) findViewById(R.id.toolbar_feedback);
        tbgoback.setNavigationIcon(R.drawable.arrow_left_d);
        tbgoback.setTitle("意见和反馈");
        //点导航栏图标退出
        tbgoback.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        comments= (EditText) findViewById(R.id.comments_feedback);
        tell= (EditText) findViewById(R.id.tell_feedback);
        submit= (Button) findViewById(R.id.submit_feedback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String content =java.net.URLEncoder.encode(comments.getText().toString());
        final String tells=tell.getText().toString();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetWorkTools.requestSubmit_feedback(tells, content, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        bean= JSON.parseObject(result, Bean.class);
                        if(bean.getCode().equals("200")){
                            Toast.makeText(Submit_feedback_Activity.this, "提交意见成功！", Toast.LENGTH_SHORT).show();
                            finish();
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
