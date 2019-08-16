package com.szg.homemakingapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.szg.homemakingapplication.accesstool.NetWorkTools;
import com.szg.homemakingapplication.accesstool.SharedHelper;
import com.szg.homemakingapplication.location.LocationService;
import com.szg.homemakingapplication.model.Bean;
import com.szg.homemakingapplication.support.adapter.fragment.HomePageFragment;
import com.szg.homemakingapplication.support.adapter.fragment.PersonalCenterFragment;
import com.szg.homemakingapplication.support.adapter.fragment.TlayIndentNavigationContentFragment;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import org.xutils.common.Callback;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Toolbar tbtitle;
    TabLayout tlaynavigation;
    private SharedHelper sharedHelper;
    private LocationService locationService;
    private Bean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        bean=new Bean();
        tbtitle= (Toolbar) findViewById(R.id.tbtitle);
        tbtitle.setNavigationIcon(R.mipmap.ic_launcher); //设置导航栏图标
        tbtitle.setTitle("首页");//设置主标题;
        tlaynavigation= (TabLayout) findViewById(R.id.tlaynavigation);
        //默认显示首页界面  “科学”参数没用
        getSupportFragmentManager().beginTransaction().replace(R.id.laycontent, HomePageFragment.newInstance("科学")).commit();
        //设置底部导航栏标题
        tlaynavigation.addTab(tlaynavigation.newTab().setText("首页"));
        tlaynavigation.addTab(tlaynavigation.newTab().setText("订单"));
        tlaynavigation.addTab(tlaynavigation.newTab().setText("个人"));
        tlaynavigation.setTabGravity(TabLayout.GRAVITY_FILL);
        tlaynavigation.setTabMode(TabLayout.MODE_FIXED);
        //读取本地文件 查看是否是休息中状态
        sharedHelper=new SharedHelper(this);
        Map<String,Object> data = sharedHelper.read();
        if(data.get("is_rest").toString().equals("false")){
            tbtitle.getMenu().clear();
            Start_location_services();
            Open_the_push_service();
            NetWorkTools.requestPosition_to_upload("1", MyApplication.locationBean.getBaiduMapLat(), MyApplication.locationBean.getBaiduMapLng(), new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    bean= JSON.parseObject(result,Bean.class);
                    if(bean.getCode().equals("200")){
                        Toast.makeText(MainActivity.this, "接单中", Toast.LENGTH_SHORT).show();
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
            tbtitle.inflateMenu(R.menu.main_toolbar_menu_order);
        }else{
            tbtitle.getMenu().clear();
            tbtitle.inflateMenu(R.menu.main_tooblar_menu_rest);
            Toast.makeText(MainActivity.this, "休息中", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //toolbar Menu单击事件
        tbtitle.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                SharedPreferences sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                switch (menuItemId){
                    //接单中单击事件
                    case R.id.is_order:
                        editor.putBoolean("is_rest",true).commit();
                        tbtitle.getMenu().clear();
                        tbtitle.inflateMenu(R.menu.main_tooblar_menu_rest);
                        NetWorkTools.requestPosition_to_upload("0", MyApplication.locationBean.getBaiduMapLat(), MyApplication.locationBean.getBaiduMapLng(), new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                bean= JSON.parseObject(result,Bean.class);
                                if(bean.getCode().equals("200")){
                                    Toast.makeText(getApplication(), "休息中", Toast.LENGTH_SHORT).show();
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
                        locationService.stop(); //停止定位服务
                        close_the_push_service();//停止推送服务
                        break;
                    //休息中单击事件
                    case R.id.is_rest:
                        editor.putBoolean("is_rest",false).commit();
                        tbtitle.getMenu().clear();
                        tbtitle.inflateMenu(R.menu.main_toolbar_menu_order);
                        Start_location_services();
                        Open_the_push_service();
                        NetWorkTools.requestPosition_to_upload("1", MyApplication.locationBean.getBaiduMapLat(), MyApplication.locationBean.getBaiduMapLng(), new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                bean= JSON.parseObject(result,Bean.class);
                                if(bean.getCode().equals("200")){
                                    Toast.makeText(MainActivity.this, "接单中", Toast.LENGTH_SHORT).show();
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
                        break;
                }
                return true;
            }
        });
        //底部导航单击事件
        tlaynavigation.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tag=tab.getText().toString();
                if(tag.equals("首页")){
                    tbtitle.setTitle("首页");
                    getSupportFragmentManager().beginTransaction().replace(R.id.laycontent, HomePageFragment.newInstance("科学")).commit();
                } else if(tag.equals("订单")){
                    tbtitle.setTitle("订单");
                    getSupportFragmentManager().beginTransaction().replace(R.id.laycontent, TlayIndentNavigationContentFragment.newInstance("科学")).commit();
                }else if(tag.equals("个人")){
                    tbtitle.setTitle("个人中心");
                    getSupportFragmentManager().beginTransaction().replace(R.id.laycontent, PersonalCenterFragment.newInstance("科学")).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    //开启定位服务
    protected void Start_location_services(){
        locationService = MyApplication.locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(NetWorkTools.mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }
    //开启推送服务
    public void Open_the_push_service(){
        XGPushConfig.enableDebug(getApplicationContext(), true);
        // 0.注册数据更新监听器
        Context context =getApplicationContext();
        // 注册接口
        XGPushManager.registerPush(getApplicationContext(),MyApplication.userBean.getUserId()+"",
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Toast.makeText(MainActivity.this,"+++ register push sucess. token:" + data,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Toast.makeText(MainActivity.this,"+++ register push fail. token:" + data
                                + ", errCode:" + errCode + ",msg:"
                                + msg,Toast.LENGTH_SHORT).show();
                    }
                });

        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);
    }
    public void close_the_push_service(){
        XGPushManager.unregisterPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
            }

            @Override
            public void onFail(Object o, int i, String s) {

            }

        });
        Context context =getApplicationContext();

        Intent service = new Intent(context, XGPushService.class);
        context.stopService(service);

    }
}
