package com.extop.education;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.Fragment.MessageListFragment;
import com.extop.education.Fragment.MyGroupFragment;
import com.extop.education.Fragment.PersonalCenterFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;


import org.json.JSONObject;
import org.xutils.common.Callback;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static com.extop.education.Adapter.SoftHideKeyBoardUtil.assistActivity;


public class MainActivity extends AppCompatActivity implements RongIM.UserInfoProvider {
//    TextView tv_myGroup, tv_personalCenter;
    TextView tv_title;
    private long exitTime = 0;
    ImageView iv_myGroup,iv_personalCenter,iv_message;
    FrameLayout frameLayout;
    View fragment;
    String userId="",name="",portraitUri="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改状态栏颜色
        Log.d("手机厂商", android.os.Build.BRAND);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (android.os.Build.BRAND.equals("Huawei")){
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }else{
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
            /*View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_STABLE*/
        }else{
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setTintColor(Color.parseColor("#029AE5"));
        }


        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //解决键盘遮挡
        assistActivity(this);

        //记录打开的Activity方便全部关闭
        MyApplication.addActivity(this);


        tv_title = (TextView) findViewById(R.id.tbtitle);
        tv_title.setText("工作圈");//Toolbar设置标题
        //默认展示我的圈子Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, MyGroupFragment.newInstance("日报")).commit();
        iv_myGroup = (ImageView) findViewById(R.id.iv_myGroup);
        //我的圈子设置为选中状态
        iv_myGroup.setSelected(true);
        //个人中心按钮
        iv_personalCenter = (ImageView) findViewById(R.id.iv_personalCenter);
        //消息按钮
        iv_message= (ImageView) findViewById(R.id.iv_message);
        //天企通目前不开放即时通讯功能
        if (MyApplication.addressType==2){
            iv_message.setVisibility(View.GONE);
        }
        //个人中心、工作圈父容器
        frameLayout= (FrameLayout) findViewById(R.id.frmelayout);
        //会话列表控件
        fragment=findViewById(R.id.conversationlist);
        //设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
        RongIM.setUserInfoProvider(this,true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //监听我的圈子点击事件
        iv_myGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重置所有文本的选中状态
                setSelected();
                //设置控件为选中状态
                iv_myGroup.setSelected(true);
                //引入对应的Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, MyGroupFragment.newInstance("")).commit();
                //将背景图片设置为蓝色
                iv_myGroup.setImageResource(R.mipmap.gzq_blue);
                //Fragment设置为显示
                frameLayout.setVisibility(View.VISIBLE);
                //会话列表布局宽、高、权重都设为0
                fragment.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 0));
                //设置标题
                tv_title.setText("工作圈");
            }
        });
        iv_personalCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                iv_personalCenter.setSelected(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, PersonalCenterFragment.newInstance("")).commit();
                iv_personalCenter.setImageResource(R.mipmap.grzx_blue);
                frameLayout.setVisibility(View.VISIBLE);
                fragment.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 0));
                tv_title.setText("个人中心");
            }
        });
        iv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                iv_message.setSelected(true);
                iv_message.setImageResource(R.mipmap.xx_blue);
                fragment.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 1.0f));
                frameLayout.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, MessageListFragment.newInstance("")).commit();
                tv_title.setText("消息");
            }
        });
    }
    //重置所有文本的选中状态
    private void setSelected() {
        iv_myGroup.setSelected(false);
        iv_myGroup.setImageResource(R.mipmap.gzq);

        iv_personalCenter.setSelected(false);
        iv_personalCenter.setImageResource(R.mipmap.grzx);

        iv_message.setSelected(false);
        iv_message.setImageResource(R.mipmap.xx);
    }
    //按返回键事件
    @Override
    public void onBackPressed() {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出登录",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
    }
    //隐藏或显示底部虚拟按键，并且触摸不会弹出虚拟按键
    private void hideNavigationBar() {
        // TODO Auto-generated method stub
        final View decorView = getWindow().getDecorView();
//        final int flags =  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(flags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            }
        });
    }

    //获取GroupUserInfo
   @Override
    public UserInfo getUserInfo(String s) {
        //获取用户提供者信息
        NetWorkTools.getUserInformationProvider(s, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                    userId=json.getString("userId");
                    name=json.getString("name");
                    portraitUri=json.getString("portraitUri");
                    Log.e("mainActivity","ID"+portraitUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("错误", ex+"");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        if (userId!=""){
            return new UserInfo(userId,name, Uri.parse(MyApplication.url+"upload/"+portraitUri));
        }
        Log.e("mainActivity","userID"+s);
        return null;
    }
}
