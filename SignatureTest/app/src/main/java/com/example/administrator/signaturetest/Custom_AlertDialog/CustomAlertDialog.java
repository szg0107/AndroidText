package com.example.administrator.signaturetest.Custom_AlertDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.signaturetest.R;

/**
 * Created by Administrator on 2017/5/29.
 * 自定义Dialog
 */

public class CustomAlertDialog extends Dialog {
    private Button btn_determine,btn_cancel;
    private EditText et_content;
    private View.OnClickListener listener;
    public CustomAlertDialog(Context context) {
        super(context);
    }
    //第一个参数上下文，第二个参数风格
    public CustomAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //给dialog绑定布局
        setContentView(R.layout.mydialog);
        //获取控件
        btn_determine=(Button)findViewById(R.id.btn_determine);
        btn_determine.setOnClickListener(listener);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialog.this.dismiss();
            }
        });
        et_content= (EditText) findViewById(R.id.et_content);
        CustomAlertDialog.this.setCancelable(false);
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    public String getContent(){
        return et_content.getText().toString().trim();
    }
    public void setContent(String content){
        et_content.setText(content);
    }
    public void clearContent(){
        et_content.setText("");
    }
}
