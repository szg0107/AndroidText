package com.example.administrator.signaturetest.Download_the_PDF_and_displays;


import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.example.administrator.signaturetest.R;
import com.lidong.pdf.PDFView;
import com.lidong.pdf.listener.OnDrawListener;
import com.lidong.pdf.listener.OnLoadCompleteListener;
import com.lidong.pdf.listener.OnPageChangeListener;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.io.File;


/**
 * Created by Administrator on 2017/6/17.
 * 下载PDF通过其他应用打开
 */

public class Download_the_PDF_and_display4 extends AppCompatActivity implements OnPageChangeListener
        , OnLoadCompleteListener, OnDrawListener {
    public static final String Pdf_Url="http://59.110.226.225/upload/1486699861896_0.pdf";
    private PDFView pdfview;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        setContentView(R.layout.pdf);
        pdfview = (PDFView) findViewById(R.id.pdfView);
        downloadPdf();
    }


    private void downloadPdf(){

        try {
//            com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();
            RequestParams params = new RequestParams(Pdf_Url);
            params.setAutoResume(true);//设置是否在下载是自动断点续传
            params.setAutoRename(false);//设置是否根据头信息自动命名文件
            //下面的回调都是在主线程中运行的,这里设置的带进度的回调
            x.http().post(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    Toast.makeText(Download_the_PDF_and_display4.this,"onSuccess",Toast.LENGTH_SHORT).show();
                    Log.d("onSuccess",result.toString());
//                   File file = new File(Environment.getExternalStorageDirectory()+"/provisional.pdf");
                    displayFromAssets(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(Download_the_PDF_and_display4.this,"onError",Toast.LENGTH_SHORT).show();
                    Log.d("错误",ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    Toast.makeText(Download_the_PDF_and_display4.this,"onFinished",Toast.LENGTH_SHORT).show();
                    hideProgress();
                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                    Toast.makeText(Download_the_PDF_and_display4.this,"onStarted",Toast.LENGTH_SHORT).show();
                    showProgress();
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    Log.d("进度","当前进度:"+total+",全部进度:"+current);
                }
            });
        } catch (Exception e) {
            Log.d("error", e.toString());
        }

    }

    private void displayFromAssets(File assetFileName ) {
        pdfview.fromFile(assetFileName) //设置pdf文件地址
                .defaultPage(1)  //设置默认显示第1页
                .onPageChange(this) //设置翻页监听
                .onLoad(this)  //设置加载监听
                .onDraw(this)  //绘图监听
                .showMinimap(false) //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical(false) //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true) //是否允许翻页，默认是允许翻页
                .load();
    }

    /**
     * 翻页回调
     *
     * @param page
     * @param pageCount
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        /*Toast.makeText(Download_the_PDF_and_display.this , "page= " + page +
                " pageCount= " + pageCount , Toast.LENGTH_SHORT).show();*/
    }

    /**
     * 加载完成回调
     *
     * @param nbPages 总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText(Download_the_PDF_and_display4.this, "加载完成" + nbPages, Toast.LENGTH_SHORT).show();
        hideProgress();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
        // Toast.makeText( MainActivity.this ,  "pageWidth= " + pageWidth + "
        // pageHeight= " + pageHeight + " displayedPage="  + displayedPage , Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示对话框
     */
    private void showProgress() {
        pd = new ProgressDialog(Download_the_PDF_and_display4.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
        pd.setMessage("资源加载中,请稍候...");// 设置ProgressDialog提示信息
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        pd.setIndeterminate(false);
        pd.setCancelable(false); // 设置ProgressDialog 是否可以按退回键取消
        pd.show();
    }

    /**
     * 关闭等待框
     */
    private void hideProgress() {
        pd.dismiss();
    }
}
