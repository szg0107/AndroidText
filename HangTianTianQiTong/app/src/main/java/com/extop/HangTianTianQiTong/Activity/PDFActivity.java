package com.extop.HangTianTianQiTong.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.extop.HangTianTianQiTong.Adapter.NetWorkTools;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

import static com.extop.HangTianTianQiTong.Adapter.SoftHideKeyBoardUtil.assistActivity;
//文案详情
public class PDFActivity extends AppCompatActivity implements OnPageChangeListener {
    public static String Pdf_Url;
    private int pageNumber = 1;
    private PDFView pdfview;
    private Toolbar tb_icon;
    private Intent intent;
    private ProgressDialog pd;
    private String url= MyApplication.url+"upload/",pdf_url="",pdfPath="";
    private File file;
    private int progressStart = 0;
    private int maxValue = 100;
    //定义一个用于更新进度的Handler,因为只能由主线程更新界面,所以要用Handler传递信息
    final Handler hand = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            //这里的话如果接受到信息码是123
//            if(msg.what == 123)
//            {
//                //设置进度条的当前值
//                pd.setProgress(progressStart);
//            }
            int current=msg.arg1;
            int total=msg.arg2;
            Log.d("current1213",current+"");
            Log.d("total4143242",total+"");
            progressStart=(int)((current*1.0/total*1.0)*100);
            Log.d("ji",progressStart+"");
//            progressStart=current/total;
            pd.setProgress(progressStart);
            //如果当前大于或等于进度条的最大值,调用dismiss()方法关闭对话框
            if(progressStart >= maxValue)
            {
                pd.dismiss();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改状态栏颜色
        Window window=getWindow();
        NetWorkTools.taskbar_transparent(window,this);


        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        setContentView(R.layout.activity_pdfview);
        //键盘弹起窗口可拉伸
        assistActivity(this);
        pdfview = (PDFView) findViewById(R.id.pdfview);
        tb_icon= (Toolbar) findViewById(R.id.pdf_icon);
        tb_icon.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_icon.inflateMenu(R.menu.file_transfer);
        tb_icon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        intent=getIntent();

        pdf_url=intent.getStringExtra("pdf");
        if (pdf_url.contains("http")){
            Pdf_Url =pdf_url;
        }else {
            Pdf_Url = url+pdf_url;
            Log.d("pdf",pdf_url);
            Log.d("地址", Pdf_Url);
        }
//        Pdf_Url=url+intent.getStringExtra("pdf");
        file = new File(Environment.getExternalStorageDirectory()+"/"+pdf_url);

        tb_icon.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //由文件得到uri
                pdfPath=Environment.getExternalStorageDirectory()+"/"+pdf_url;
                Uri pdfUri = Uri.fromFile(new File(pdfPath));
                Log.d("share", "uri:" + pdfUri);  //输出：file:///storage/emulated/0/test.jpg

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                shareIntent.setType("application/pdf");
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                return false;
            }
        });

        //检测读写权限方法
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            if (file.exists()){
                readPdf(file);
            }else{
                downloadPdf();
            }

        }
        //需要弹出dialog让用户手动赋予权限
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }



    }


    private void downloadPdf(){

        try {
            com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();
            http.download(Pdf_Url,  Environment.getExternalStorageDirectory()+"/"+pdf_url, true, false, new RequestCallBack<File>() {
                @Override
                public void onStart() {
                    pd=new ProgressDialog(PDFActivity.this);
                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度条风格，风格为圆形，旋转的
                    pd.setMessage("资源加载中,请稍候...");// 设置ProgressDialog提示信息
                    pd.setMax(maxValue);
                    // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
                    pd.setIndeterminate(false);
                    pd.setCancelable(false); // 设置ProgressDialog 是否可以按退回键取消
                    pd.show(); // 让ProgressDialog显示
                }
                @Override
                public void onLoading( long total,  long current, boolean isUploading) {
//                    Toast.makeText(PDFActivity.this,"下载中",Toast.LENGTH_SHORT).show();
                    Message message = Message.obtain();
                    message.what = 123;
                    message.arg1 = (int)current;
                    message.arg2 =(int) total;
                    //把信息码发送给handle让更新界面
                    hand.sendMessage(message);
                }
                @Override
                public void onFailure(HttpException error, String msg) {
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
//                    Toast.makeText(PDFActivity.this,"完成",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    readPdf(file);
                }
            });
        } catch (Exception e) {
            Log.d("error", e.toString());
        }

    }

    private void readPdf(File file) {
        pdfview.fromFile(file)
                .defaultPage(pageNumber)
                .load();
    }
    //这个下载网络上的pdf到sd卡上的代码  然后进行本地读取  在失去焦点的时候删除下载的文件
//    删除代码
    @Override
    protected void onPause() {
        super.onPause();
        /*File file = new File(Environment.getExternalStorageDirectory()+"/"+pdf_url);
        if (file.exists()) {
            file.delete();
        }*/

    }


    //继承接口后可以继承的方法
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //检测读写权限方法
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意使用write
                downloadPdf();
            }else{
                //用户不同意，向用户展示该权限作用
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("需要赋予访问存储的权限，不开启将无法正常工作！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                    return;
                }
                finish();
            }
        }
    }
}