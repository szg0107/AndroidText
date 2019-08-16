package com.extop.beikongsanitation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.extop.beikongsanitation.Activity.LoginActivity;
import com.extop.beikongsanitation.Fragment.OperationRecordsFragment;
import com.extop.beikongsanitation.Fragment.WorkStationFragment;
import com.extop.beikongsanitation.Tool.NetWorkTools;
import com.extop.beikongsanitation.Tool.PublicActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.SimpleDateFormat;
import java.util.Date;

/*主页*/
public class MainActivity extends PublicActivity implements View.OnClickListener {
    private TextView tv_workStation, tv_operationRecords, tb_title;
    private Toolbar toolbar;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //默认展示操作台Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, WorkStationFragment.newInstance("操作台")).commit();
        //获取操作台按钮并绑定点击事件
        tv_workStation = findViewById(R.id.tv_workStation);
        tv_workStation.setOnClickListener(this);
        //获取操作记录按钮并绑定点击事件
        tv_operationRecords = findViewById(R.id.tv_operationRecords);
        tv_operationRecords.setOnClickListener(this);
        // 获取Toolbar标题
        tb_title = findViewById(R.id.tb_title);
        // 获取Toolbar控件
        toolbar = findViewById(R.id.toolbar);
        //给Toolbar添加菜单
        toolbar.inflateMenu(R.menu.logout);
        //给Toolbar的Menu设置点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    // 登出
                    case R.id.logout:
                        Log.d("isOpen", "登出被点击");
                        outLoginDialog();
                        break;
                }
                return false;
            }
        });

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

                        NetWorkTools.OutLoginFlatByDriver(MyApplication.identificationCode + "", aMapLocation.getLatitude() + "", aMapLocation.getLongitude() + "", MyApplication.loginId + "", new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d("返回的值", NetWorkTools.replaceBlank(result));
                                try {
                                    //获取整个JSON对象
                                    JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                                    //获取dataJSONArray中的JSON对象
                                    JSONObject data = json.getJSONArray("data").getJSONObject(0);
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("错误", e + "");
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
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
    }

    //重置所有文本的选中状态
    private void setSelected() {
        tv_workStation.setSelected(false);
        tv_operationRecords.setSelected(false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            // 操作台
            case R.id.tv_workStation:
                //重置所有文本的选中状态
                setSelected();
                //设置控件为选中状态
                tv_workStation.setSelected(true);
                //引入对应的Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, WorkStationFragment.newInstance("操作台")).commit();
                //设置标题
                tb_title.setText("操作台");
                //设置字体颜色
                tv_operationRecords.setTextColor(Color.rgb(189, 189, 189));
                tv_workStation.setTextColor(Color.rgb(2, 154, 229));
                break;
            //  操作记录
            case R.id.tv_operationRecords:
                //重置所有文本的选中状态
                setSelected();
                //设置控件为选中状态
                tv_workStation.setSelected(true);
                //引入对应的Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.frmelayout, OperationRecordsFragment.newInstance("操作记录")).commit();
                //设置标题
                tb_title.setText("操作记录");
                //设置字体颜色
                tv_operationRecords.setTextColor(Color.rgb(2, 154, 229));
                tv_workStation.setTextColor(Color.rgb(189, 189, 189));
                break;
        }
    }

    //按返回键事件
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次登出",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            outLoginDialog();
        }
    }


    public void outLoginDialog(){
        if (MyApplication.operateType==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
            builder.setMessage("是否要登出？");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    mLocationClient.startLocation();
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
        }else {
            //1 加水 2 倾倒 3 排污
            switch (MyApplication.operateType){
                case 1:
                    Toast.makeText(MainActivity.this,"您正在加水，无法登出，请点击加水完毕后再登出。",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this,"您正在倾倒，无法登出，请点击倾倒完毕后再登出。",Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(MainActivity.this,"您正在排污，无法登出，请点击排污完毕后再登出。",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
