package com.extop.education.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.Adapter.ScaleImageView;
import com.extop.education.MyApplication;
import com.extop.education.R;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.extop.education.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/3/29.
 * 问题详情
 */

public class QuestionDetailsActivity extends AppCompatActivity {
    private Toolbar tb_icon;
    private XWalkView wv_QuestionDetails;
    private Intent intent;
    private EditText ed_answer;
    private Button bt_send;
    InputMethodManager imm;
    String url = MyApplication.url+"questionDetails.view?id=";
    String QuestionID;
    private ScaleImageView scaleImageView;
    private String imgurl = "";
    private ProgressDialog pd;
    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.cancel();
                    scaleImageView.setVisibility(View.VISIBLE);
                    Bitmap bmp = (Bitmap) msg.obj;
                    scaleImageView.setImageBitmap(bmp);
                    break;
            }
        }

    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改状态栏颜色
        Window window=getWindow();
        NetWorkTools.taskbar_transparent(window,this);


        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //绑定布局
        setContentView(R.layout.question_details);

        //键盘弹起窗口可拉伸
        assistActivity(this);
        //获取控件
        scaleImageView = (ScaleImageView) findViewById(R.id.question_image);
        tb_icon= (Toolbar) findViewById(R.id.questionDetails_icon);
        ed_answer= (EditText) findViewById(R.id.answer);
        bt_send= (Button) findViewById(R.id.send);
        imm= (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        tb_icon.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        wv_QuestionDetails= (XWalkView) findViewById(R.id.wv_QuestionDetails);
        intent=getIntent();
        NetWorkTools.XWalkView_Settings(wv_QuestionDetails,this);
        QuestionID=intent.getStringExtra("id");
        wv_QuestionDetails.loadUrl(url+QuestionID+"&circleID="+MyApplication.circleID+"&reuse="+intent.getStringExtra("reuse")+"&isTask="+intent.getBooleanExtra("isTask",false));//调用loadView方法为WebView加入链接
        Log.d("问题详情", url+QuestionID+"&circleID="+MyApplication.circleID+"&reuse="+intent.getStringExtra("reuse")+"&isTask="+intent.getBooleanExtra("isTask",false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_answer.getText().length()!=0){
                    //调用sendAnswer方法回答问题
                    wv_QuestionDetails.loadUrl("javascript:sendAnswer('"+ed_answer.getText().toString()+"')");
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(QuestionDetailsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    ed_answer.setText("");
                    ed_answer.clearFocus();
                    Toast.makeText(getApplicationContext(),"回答成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"回复内容不能为空!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @JavascriptInterface
    public void setValue(String string) {
        if (string.length()>16){
            imgurl=string;
            pd=new ProgressDialog(QuestionDetailsActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
            pd.setMessage("资源加载中,请稍后...");// 设置ProgressDialog提示信息
            // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
            pd.setIndeterminate(false);
            pd.setCancelable(false); // 设置ProgressDialog 是否可以按退回键取消
            pd.show(); // 让ProgressDialog显示
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Bitmap bmp = getURLimage(imgurl);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = bmp;
                    System.out.println("000");
                    handle.sendMessage(msg);
                }
            }).start();
        }else {
            intent=new Intent("android.intent.Activity.ReplyForDetailsActivity");
            intent.putExtra("replyID",string);
            intent.putExtra("QuestionID",QuestionID);
            startActivity(intent);
        }
    }

    @JavascriptInterface
    public void getValue(String string) {
        if (string.equals("hello android!!")){
            finish();
        }
        if (string.equals("deleteQuestion")){
            dialogs();
        }
    }

    protected void dialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionDetailsActivity.this, R.style.AlertDialog);
        builder.setMessage("您确定要删除本条信息吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //webView在另外的一个线程中使用loadUrl
                wv_QuestionDetails.post(new Runnable() {
                    @Override
                    public void run() {
                        wv_QuestionDetails.loadUrl("javascript:delete_Question('" + QuestionID + "')");
                    }
                });

                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(true);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
    //按返回键事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int visibility = scaleImageView.getVisibility();
        /*View.VISIBLE 常量值为0，意思是可见的
        *View.INVISIBLE 常量值为4，意思是不可见的
        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
        * */
        wv_QuestionDetails.getNavigationHistory().clear();
        if (visibility == 8 && keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        } else if (visibility == 0 && keyCode == KeyEvent.KEYCODE_BACK) {
            scaleImageView.setVisibility(View.GONE);
        }
        return false;
    }
}
