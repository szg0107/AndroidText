package com.extop.beikongsanitation.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.extop.beikongsanitation.MyApplication;
import com.extop.beikongsanitation.R;
import com.extop.beikongsanitation.Tool.NetWorkTools;
import com.extop.beikongsanitation.Tool.PublicActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/18.
 * 登录
 */

public class LoginActivity extends PublicActivity implements View.OnClickListener {
    //登录按钮
    private Button login;
    //用户名、工号
    private EditText driverName, driverCode;
    //检测读写权限方法
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    // 安装包地址
    private String apkURL="";

    ProgressDialog dialog;

    // 显示设备号
//    private EditText test;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断用户是否允许访问手机识别码
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_EXTERNAL_STORAGE);
        }
        //获取IMEI唯一识别码
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        MyApplication.identificationCode = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
//        Toast.makeText(LoginActivity.this,MyApplication.identificationCode,Toast.LENGTH_LONG).show();
        Log.d("IMEI", MyApplication.identificationCode);
        //绑定布局
        setContentView(R.layout.activity_login);
        //获取按钮
        login = findViewById(R.id.login);
        //登录绑定事件
        login.setOnClickListener(this);
        //获取用户名和工号控件
        driverName = findViewById(R.id.driverName);
        driverCode = findViewById(R.id.driverCode);


//        test=findViewById(R.id.test);
//        test.setText(MyApplication.identificationCode);


        // 定位回调
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.d("定位结果", aMapLocation.toString());
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        //获取定位时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);
                        //给经度、纬度、时间、地址赋值
//                        longitude = aMapLocation.getLongitude() + "";
//                        latitude = aMapLocation.getLatitude() + "";
                        MyApplication.times = df.format(date);
                        MyApplication.address = aMapLocation.getCity() + "," + aMapLocation.getDistrict() + "," + aMapLocation.getStreet() + "," + aMapLocation.getStreetNum();
                        Log.d("信息", aMapLocation.getLatitude() + ","
                                + aMapLocation.getLongitude() + ","
                                + aMapLocation.getProvince() + ","
                                + aMapLocation.getCity() + ","
                                + aMapLocation.getDistrict() + ","
                                + aMapLocation.getStreet() + ","
                                + aMapLocation.getStreetNum() + ","
                                + df.format(date)
                        );
                        NetWorkTools.getLoginFlatByDriver(driverName.getText().toString() + "", driverCode.getText().toString() + "", MyApplication.identificationCode + "", aMapLocation.getLatitude() + "", aMapLocation.getLongitude() + "", new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d("返回的值", NetWorkTools.replaceBlank(result));
                                try {
                                    JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                                    JSONObject data = json.getJSONArray("data").getJSONObject(0);
                                    MyApplication.driverId = data.getString("driverId");
                                    MyApplication.driverName = data.getString("driverName");
                                    MyApplication.driverCode = data.getString("driverCode");
                                    MyApplication.carId = data.getString("carId");
                                    MyApplication.loginId = data.getString("loginId");
                                    MyApplication.carNum = data.getString("carNum");
                                    //获取车辆信息
                                    NetWorkTools.GetCarTypeByCarId(MyApplication.identificationCode + "", MyApplication.carId + "", new Callback.CommonCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            Log.d("车辆信息", NetWorkTools.replaceBlank(result));
                                            try {
                                                // 得到JSON对象
                                                JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                                                // 将dataJsonArray对象转换为JSON对象
                                                JSONObject data = json.getJSONArray("data").getJSONObject(0);
                                                MyApplication.carType = data.getString("Name");
                                                MyApplication.Id = data.getString("Id");
                                                MyApplication.typeStatus=data.getBoolean("TypeStatus");
                                                Intent intent = new Intent("android.intent.Activity.MainActivity");
                                                startActivity(intent);
                                                finish();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("错误", e + "");
                                                Toast.makeText(LoginActivity.this, "没有获取到车辆信息", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable ex, boolean isOnCallback) {
                                            Toast.makeText(LoginActivity.this, "获取车辆信息失败", Toast.LENGTH_SHORT).show();
                                            Log.e("错误", ex + "");
                                        }

                                        @Override
                                        public void onCancelled(CancelledException cex) {

                                        }

                                        @Override
                                        public void onFinished() {

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "用户名或工号错误或者用户名与设备不匹配,请确认后重新输入！", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Log.e("错误", ex + "");
                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        if (aMapLocation.getErrorCode()==12){
                            Toast.makeText(LoginActivity.this,"您没有授予应用定位权限，无法进行后续操作！",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);



        dialog = new ProgressDialog(this);
        //判断apk是否存在 存在删除
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "provisional.apk");
        if (file.exists()) {
            Log.d("地址","文件存在");
            file.delete();
        }
        //得到服务器上的版本信息
        NetWorkTools.GetVersion(MyApplication.identificationCode, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("返回的值", NetWorkTools.replaceBlank(result));
                try {
                    //获取全部返回值
                    JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                    JSONObject data = json.getJSONArray("data").getJSONObject(0);
                    apkURL=data.getString("url");
                    Log.d("地址",Environment.getExternalStorageDirectory() + File.separator);

                    if(Double.parseDouble(getVersion())<Double.parseDouble(data.getString("banben"))){

                        //检测读写权限方法
                        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
                            dialogs();
                        } else {
                            //需要弹出dialog让用户手动赋予权限
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                        }

//                        Toast.makeText(LoginActivity.this,"有新版本请更新",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("错误", ex + "");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    //检测读写权限方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用write
                downloadUpdateApk();
            } else {
                //用户不同意，向用户展示该权限作用
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("需要赋予访问存储的权限，不开启将无法正常更新！")
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
    //提示的方法
   /**@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用write
            } else {
                //用户不同意，向用户展示该权限作用
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("需要赋予访问手机识别码，不开启将无法正常工作！")
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
    }**/

    // 登录点击方法
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            // 登录
            case R.id.login:
                //启动定位
                if (driverName.length() > 0 && driverCode.length() > 0) {
                    mLocationClient.startLocation();
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或工号不能为空,请输入！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //获取当前版本
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

    // 弹窗提示
    protected void dialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialog);
        builder.setMessage("您当前版本不是最新版是否进行更新？");
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                downloadUpdateApk();
                dialog.dismiss();
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

    /**
     * 下载服务器端更新后最新的apk
     */
    private void downloadUpdateApk() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            // apkURL为JSON从服务器端解析出来的下载地址
            RequestParams requestParams = new RequestParams(apkURL);
            // 为RequestParams设置文件下载后的保存路径
            requestParams.setSaveFilePath(path);
            // 下载完成后自动为文件命名
            requestParams.setAutoRename(false);
            x.http().get(requestParams, new Callback.ProgressCallback<File>() {

                @Override
                public void onSuccess(File result) {
                    installApk(result);
                    Log.i("消息", "下载成功"+result.getName()+result.getPath());
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("消息", ex+"");
                    Log.i("消息", "下载失败");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.i("消息", "取消下载");
                }

                @Override
                public void onFinished() {
                    dialog.dismiss();
                    Log.i("消息", "结束下载");
                }

                @Override
                public void onWaiting() {
                    // 网络请求开始的时候调用
                    Log.i("消息", "等待下载");
                }

                @Override
                public void onStarted() {
                    // 下载的时候不断回调的方法
                    Log.i("消息", "开始下载");
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    // 当前的下载进度和文件总大小
                    Log.i("消息", "正在下载中......");
//                    Toast.makeText(LoginActivity.this,"正在下载中",Toast.LENGTH_LONG).show();
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
                    dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                    dialog.setTitle("程序正在更新请稍候……");
                    int max = (int) total / 1024;
                    int progress = (int) current / 1024;
                    dialog.setMax(max);// 设置进度条的最大值
                    dialog.setProgress(progress);// 设置当前进度
                    dialog.show();
                }
            });
        }
    }
}
