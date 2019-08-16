package com.example.administrator.signaturetest.Download_the_PDF_and_displays;

import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.signaturetest.R;
import com.lidong.pdf.PDFView;
import com.lidong.pdf.listener.OnDrawListener;
import com.lidong.pdf.listener.OnLoadCompleteListener;
import com.lidong.pdf.listener.OnPageChangeListener;

/**
 * Created by Administrator on 2017/6/17.
 * 下载PDF本地阅读
 */

public class Download_the_PDF_and_display extends AppCompatActivity implements OnPageChangeListener
        , OnLoadCompleteListener, OnDrawListener {
    private PDFView pdfView;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        displayFromFile1("http://59.110.226.225/upload/1486699861896_0.pdf", "1486699861896_0.pdf");
        //读取本地PDF
//        displayFromAssets("http://123.56.187.157/hlts/upload/1499049299321_1.pdf");
    }
    /*打开本地文件*/
    private void displayFromAssets(String assetFileName ) {
        pdfView.fromAsset(assetFileName) //设置pdf文件地址
                .defaultPage(1)  //设置默认显示第1页
                .onPageChange(this) //设置翻页监听
                .onLoad(this)  //设置加载监听
                .onDraw(this)  //绘图监听
                .showMinimap(true) //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical(true) //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true) //是否允许翻页，默认是允许翻页
                .load();
    }
    /**
     * 获取打开网络的pdf文件
     *
     * @param fileUrl
     * @param fileName
     */
    private void displayFromFile1(String fileUrl, String fileName) {
        showProgress();
        pdfView.fileFromLocalStorage(this, this, this, fileUrl, fileName);   //设置pdf文件地址

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
        Toast.makeText(Download_the_PDF_and_display.this, "加载完成" + nbPages, Toast.LENGTH_SHORT).show();
        hideProgress();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
        // Toast.makeText( CustomAlertDialogActivity.this ,  "pageWidth= " + pageWidth + "
        // pageHeight= " + pageHeight + " displayedPage="  + displayedPage , Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示对话框
     */
    private void showProgress() {
        pd = new ProgressDialog(Download_the_PDF_and_display.this);
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
