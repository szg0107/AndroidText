package com.extop.education.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.extop.education.Adapter.ToolbarWebViewActivity;
import com.extop.education.MyApplication;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkCookieManager;

/**
 * Created by Administrator on 2017/11/23.
 * 商品详情
 */

public class ProductDetailsActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "product_details.view?id=", ProductId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("商品详情");
        ProductId = intent.getStringExtra("ProductId");
        xWalkView.loadUrl(url + ProductId);
        Log.d("商品详情地址", url + ProductId);
    }

    @JavascriptInterface
    public void setValue(String string) {
        String type = string.substring(string.length() - 3);
        if (type.equals("pdf")) {
            intent = new Intent("android.intent.Activity.PDFActivity");
            intent.putExtra("pdf", string);
            startActivity(intent);
        } else {
            intent = new Intent("android.intent.Activity.CoursewareDetailsActivity");
            intent.putExtra("courseware", string);
            startActivity(intent);
        }
    }
}
