package com.example.administrator.xianxiaapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.administrator.xianxiaapplication.Fragment.HomeFragment;
import com.example.administrator.xianxiaapplication.Fragment.NewsFragment;
import com.example.administrator.xianxiaapplication.Fragment.ReadingFragment;
import com.example.administrator.xianxiaapplication.Fragment.ScienceFragment;
import com.example.administrator.xianxiaapplication.Instance.ArticleBean;

import static com.example.administrator.xianxiaapplication.R.attr.actionModeCloseDrawable;

/**
 * Created by Administrator on 2016/8/3.
 */

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private long exitTime = 0;
    private Toolbar tb;
    private SQLiteDatabase db;
    private MyDBOpenHelper myDBHelper;
    private int pid=0;
    private ArticleBean articleBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webviewcontent);
        Intent intent = getIntent();
        articleBean = (ArticleBean) intent.getSerializableExtra("url");
        myDBHelper = new MyDBOpenHelper(this, "Mydata.db", null, 1);
        tb = (Toolbar) findViewById(R.id.webToolbar);
        //设置导航栏图标
        tb.setNavigationIcon(R.drawable.arrow_left_d);
        //设置右上角的填充菜单
        db = myDBHelper.getWritableDatabase();
        //参数依次是:表名，列名，where约束条件，where中占位符提供具体的值，指定group by的列，进一步约束
        //指定查询结果的排序方式
        Cursor cursor = db.query("ArticleBean", null,"title=?" , new String[]{articleBean.getTitle()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                pid= cursor.getInt(cursor.getColumnIndex("is_collected"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (pid ==0) {
            tb.inflateMenu(R.menu.webwiew_toolbar_menu);
        } else {
            tb.inflateMenu(R.menu.webwiew_toolbar_menu2);
        }
        //点导航栏图标退出
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                switch (menuItemId) {
                    case R.id.star_white:
                        articleBean.setIs_collected(1);
                        ContentValues values1 = new ContentValues();
                        values1.put("title", articleBean.getTitle());
                        values1.put("url",articleBean.getUrl());
                        values1.put("small_image",articleBean.getSmall_image());
                        values1.put("is_collected",articleBean.getIs_collected());
                        //参数依次是：表名，强行插入null值得数据列的列名，一行记录的数据
                        db.insert("ArticleBean", null, values1);
                        Toast.makeText(WebViewActivity.this, "成功添加到收藏！", Toast.LENGTH_LONG).show();

                        tb.getMenu().clear();
                        tb.inflateMenu(R.menu.webwiew_toolbar_menu2);
                        break;
                    case R.id.mtrl_alpha:
                        Toast.makeText(WebViewActivity.this, "分享成功！", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.star_black:
                        //参数依次是表名，以及where条件与约束
                        db.delete("ArticleBean", "url = ?", new String[]{articleBean.getUrl()});
                        articleBean.setIs_collected(0);
                        Toast.makeText(WebViewActivity.this, "成功从收藏移除！", Toast.LENGTH_LONG).show();
                        tb.getMenu().clear();
                        tb.inflateMenu(R.menu.webwiew_toolbar_menu);
                        break;
                }
                return true;
            }
        });
        webView = (WebView) findViewById(R.id.webviewtext);
        webView.setWebViewClient(new WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        webView.loadUrl(articleBean.getUrl());          //调用loadView方法为WebView加入链接
        //setContentView(webView);                           //调用Activity提供的setContentView将webView显示出来
    }



    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                super.onBackPressed();
            }
        }

    }

    public class MyDBOpenHelper extends SQLiteOpenHelper {
        public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            //1.上下文 2.数据库名字 3.   4.版本号
            super(context, "Mydata.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE ArticleBean(articleBeanid INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR(200),url VARCHAR(200),small_image VARCHAR(200),is_collected INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("ALTER TABLE ArticleBean ADD phone VARCHAR(12) NULL");
        }
    }
}
