package com.extop.education.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.MyApplication;
import com.extop.education.R;
import com.extop.education.Receiver.MyReceiver;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkView;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/4/19.
 * 启动动画界面
 */

public class Start_Animation extends AppCompatActivity implements MyReceiver.BRInteraction {
    ProgressDialog dialog;
    String Apk_Url;
    XWalkView wv_Change;
    AlphaAnimation aa;//动画
    Boolean isSame, isNet; //isSame apk版本是否相同 isNet 手机是否有网
    MyReceiver myReceiver;//监听网络服务

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.start_animation, null);

        //根据地址类别显示动画的背景
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.version_linear);
        if (MyApplication.addressType == 2) {
            linearLayout.setBackground(getResources().getDrawable(R.drawable.animations_tqt));
        } else {
            linearLayout.setBackground(getResources().getDrawable(R.drawable.animations));
        }


        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);

        //渐变展示启动屏
        aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);

        dialog = new ProgressDialog(this);
        //是否有网
        isNet = isNetworkConnected(this);
        //如果没有网启动BroadcastReceiver，实现网络状态实时监听
        if (!isNet) {
            myReceiver = new MyReceiver();
            IntentFilter itFilter = new IntentFilter();
            itFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(myReceiver, itFilter);
            myReceiver.setBRInteractionListener(this);
        }

        wv_Change = (XWalkView) findViewById(R.id.version);
        NetWorkTools.XWalkView_Settings(wv_Change, this);
        //下载APK
        File file = new File(Environment.getExternalStorageDirectory() + "/provisional.apk");
        if (file.exists()) {
            file.delete();
        }
        if (isNet) {
            wv_Change.loadUrl(MyApplication.url + "Detectionversion.view?PhoneSys=Android");//调用loadView方法为WebView加入链接
        } else {
            Toast.makeText(this, "请检查您的网络", Toast.LENGTH_LONG).show();
        }

        MyApplication.is_Virtual_key = checkDeviceHasNavigationBar();
    }

    /*在Activity,onCreate时加载版本页面，
    页面调用原生方法，原生根据返回值判断版本号是否相同，
    不相同给Apk_Url赋值并从服务器下载最新Apk然后安装
    * */
    @JavascriptInterface
    public void setValue(String version, String apk) {
//        Toast.makeText(this,"页面调用原生方法",Toast.LENGTH_SHORT).show();
        if (Double.parseDouble(version) <= Double.parseDouble(getVersion())) {
            //当前版本是否和后台版本相同version.equals(getVersion())
            Log.d("数据库version", version);
            Log.d("APKversion", getVersion());
            Log.d("url", MyApplication.url);
            Log.d("apk", apk);
            Log.d("apk完整地址", MyApplication.url + "upload/" + apk);
            isSame = true;
            //监听动画运行过程
            aa.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {

                    redirectTo();


                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

            });
//            redirectTo();
        } else {
            isSame = false;
            Apk_Url = MyApplication.url + "upload/" + apk;
            //检测读写权限方法
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                downloadAPK();
            } else {
                //需要弹出dialog让用户手动赋予权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            }
        }
    }

    /**
     * 跳转到...
     * WelComeActivity.class
     */
    private void redirectTo() {
        Intent intent = new Intent(this, WelComeActivity.class);
        startActivity(intent);
        finish();
    }

    //比较当前版本
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    //这个下载网络上的APK到sd卡上的代码  然后进行本地读取  在开始的时候删除下载的文件
    @Override
    protected void onStart() {
        super.onStart();
        /*File file = new File(Environment.getExternalStorageDirectory() + "/provisional.apk");
        if (file.exists()) {
            file.delete();
        }
        if (isNet){
            wv_Change.loadUrl(MyApplication.url+"Detectionversion.view?PhoneSys=Android");//调用loadView方法为WebView加入链接
        }else{
            Toast.makeText(this,"请检查您的网络",Toast.LENGTH_LONG).show();
        }

        MyApplication.is_Virtual_key=checkDeviceHasNavigationBar();*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wv_Change != null) {
            wv_Change.pauseTimers();
            wv_Change.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wv_Change != null) {
            wv_Change.resumeTimers();
            wv_Change.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_Change != null) {
            wv_Change.onDestroy();
        }
    }

    //安装apk
    protected void installApk(File file) {
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
//                    File file= new File(fileName);
            Uri apkUri = FileProvider.getUriForFile(this, "com.extop.education.pro.fileprovider", file);//在AndroidManifest中的android:authorities值
            Log.d("7.0apkUri", apkUri + "");
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(install);
        }

        /*Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);*/
    }

    //下载APK
    private void downloadAPK() {
        try {
            com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();


            http.download(Apk_Url, Environment.getExternalStorageDirectory() + "/provisional.apk", true, false, new RequestCallBack<File>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onLoading(final long total, final long current, boolean isUploading) {
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
                    dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                    dialog.setTitle("程序正在更新请稍候……");
                    if (MyApplication.addressType != 2) {
                        //天企通目前不展示更新提示
                        dialog.setMessage("如果更新失败或出现闪退情况，您可以进行如下操作:\n1:卸载当前版本的开门，在应用商店或者应用宝中搜索开门KMoon重新下载开门应用。\n2:卸载当前版本，前往开门官网‘http://www.extop.cn/’扫码下载最新版开门。\n给您带来的不便，敬请谅解，谢谢！");
                    }
                    int max = (int) total / 1024;
                    int progress = (int) current / 1024;
                    dialog.setMax(max);// 设置进度条的最大值
                    dialog.setProgress(progress);// 设置当前进度
                    dialog.show();
                    super.onLoading(total, current, isUploading);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//
//                            while (dialog.getProgress() < dialog.getMax()) {
//                                try {
//                                    Thread.sleep(200);
//                                    // 更新进度条的进度,可以在子线程中更新进度条进度
////                                    Log.d("ccc","current:"+current+"total:"+total);
//                                    dialog.setProgress((int)current);// 设置当前进度
//                                    // dialog.incrementSecondaryProgressBy(10)//二级进度条更新方式
//                                } catch (Exception e) {
//                                    // TODO: handle exception
//                                }
//                            }
//                            // 在进度条走完时删除Dialog
//                            dialog.dismiss();
//
//                        }
//                    }).start();
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    File file = new File(Environment.getExternalStorageDirectory() + "/provisional.apk");
                    installApk(file);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {

                }
            });
        } catch (Exception e) {
            Log.d("error", e.toString());
        }

    }

    //判断是否有网络连接
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    public void setIsNet(Boolean content) {
        isNet = content;
        if (isNet) {
            wv_Change.loadUrl(MyApplication.url + "Detectionversion.view?PhoneSys=Android");//调用loadView方法为WebView加入链接

            redirectTo();
            //别忘了将广播取消掉
            unregisterReceiver(myReceiver);
        } else {
            Toast.makeText(this, "请检查您的网络", Toast.LENGTH_LONG).show();
        }
    }


    //判断是否存在虚拟按键
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }


    //检测读写权限方法
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用write
                downloadAPK();
            } else {
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
