package com.example.administrator.xianxiaapplication;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import com.example.administrator.xianxiaapplication.Fragment.Collect_GreyFragment;
import com.example.administrator.xianxiaapplication.Fragment.HomeFragment;
import com.example.administrator.xianxiaapplication.Fragment.NewsFragment;
import com.example.administrator.xianxiaapplication.Fragment.ReadingFragment;
import com.example.administrator.xianxiaapplication.Fragment.ScienceFragment;

import org.xutils.x;
public class MainActivity extends AppCompatActivity {
    Toolbar tb;
    DrawerLayout dl;
    NavigationView nv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tb = (Toolbar) findViewById(R.id.toolbar);
        dl= (DrawerLayout) findViewById(R.id.myDrawerLayout);
        nv= (NavigationView) findViewById(R.id.navigation_view);

        x.Ext.init(getApplication());

        tb.setNavigationIcon(R.drawable.xian); //设置导航栏图标
        tb.setTitle("日报");//设置主标题
        tb.setTitleTextColor(Color.WHITE);
        tb.inflateMenu(R.menu.base_toolbar_menu1);//设置右上角的填充菜单
        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, HomeFragment.newInstance("日报")).commit();

        //图标单击事件
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                switch (menuItemId){
                    case R.id.action_science:
                        tb.getMenu().clear();
                        tb.setTitle("科学");
                        tb.setTitleTextColor(Color.WHITE);
                        tb.inflateMenu(R.menu.base_toolbar_menu2);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, ScienceFragment.newInstance("科学")).commit();
                        break;
                    case R.id.action_home:
                        tb.getMenu().clear();
                        tb.setTitle("日报");
                        tb.setTitleTextColor(Color.WHITE);
                        tb.inflateMenu(R.menu.base_toolbar_menu1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, HomeFragment.newInstance("日报")).commit();
                        break;
                    case R.id.action_news:
                        tb.getMenu().clear();
                        tb.setTitle("新闻");
                        tb.setTitleTextColor(Color.WHITE);
                        tb.inflateMenu(R.menu.base_toolbar_menu3);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, NewsFragment.newInstance("新闻")).commit();
                        break;
                    case R.id.action_reading:
                        tb.getMenu().clear();
                        tb.setTitle("阅读");
                        tb.setTitleTextColor(Color.WHITE);
                        tb.inflateMenu(R.menu.base_toolbar_menu4);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, ReadingFragment.newInstance("阅读")).commit();
                        break;
                }
                return true;
            }
        });
        //点导航栏图标弹出侧滑菜单
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dl.openDrawer(GravityCompat.START);
            }
        });
        //侧滑菜单点哪项选中哪项
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            private MenuItem mPreMenuItem;
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(mPreMenuItem!=null){
                    mPreMenuItem.setChecked(false);
                }
                item.setChecked(true);
                dl.closeDrawers();
                mPreMenuItem=item;
                int mPreMenuItemId = mPreMenuItem.getItemId();
                switch (mPreMenuItemId){
                    case R.id.item_science:
                        tb.getMenu().clear();
                        tb.setTitle("科学");
                        tb.setTitleTextColor(Color.WHITE);
                        tb.inflateMenu(R.menu.base_toolbar_menu2);
                        //修改frmelayout中的内容
                        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, ScienceFragment.newInstance("科学")).commit();
                        break;
                    case R.id.item_home:
                        tb.getMenu().clear();
                        tb.setTitle("日报");
                        tb.setTitleTextColor(Color.WHITE);
                        tb.inflateMenu(R.menu.base_toolbar_menu1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, HomeFragment.newInstance("日报")).commit();
                        break;
                    case R.id.item_news:
                        tb.getMenu().clear();
                        tb.setTitle("新闻");
                        tb.setTitleTextColor(Color.WHITE);
                        tb.inflateMenu(R.menu.base_toolbar_menu3);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, NewsFragment.newInstance("新闻")).commit();
                        break;
                    case R.id.item_reading:
                        tb.getMenu().clear();
                        tb.setTitle("阅读");
                        tb.setTitleTextColor(Color.WHITE);
                        tb.inflateMenu(R.menu.base_toolbar_menu4);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, ReadingFragment.newInstance("阅读")).commit();
                        break;
                    case R.id.item_collect_grey:
                        tb.getMenu().clear();
                        tb.setTitle("收藏");
                        tb.setTitleTextColor(Color.WHITE);
                        tb.inflateMenu(R.menu.base_toolbar_menu1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, Collect_GreyFragment.newInstance("收藏")).commit();
                        break;
                    case R.id.subitem_night:
                        break;
                    case R.id.subitem_setting:
                        break;
                    case R.id.subitem_about:
                        break;
                }
                return true;
            }
        });
    }
}
