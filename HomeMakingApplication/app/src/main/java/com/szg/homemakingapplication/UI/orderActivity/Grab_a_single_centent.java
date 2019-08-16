package com.szg.homemakingapplication.UI.orderActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.szg.homemakingapplication.MyApplication;
import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.accesstool.NetWorkTools;
import com.szg.homemakingapplication.model.Bean;
import com.szg.homemakingapplication.model.NoticeParentBean;
import com.szg.homemakingapplication.model.OrderBean;

import org.xutils.common.Callback;


/**
 * Created by Administrator on 2016/9/18.
 */

public class Grab_a_single_centent extends AppCompatActivity {
    private OrderBean orderBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.grab_single_details_centent);
        Intent intent=getIntent();
        orderBean= (OrderBean) intent.getSerializableExtra("orderBeens");
        Toolbar tbgoback= (Toolbar) findViewById(R.id.toolbar_Grab);
        tbgoback.setNavigationIcon(R.drawable.arrow_left_d);
        tbgoback.setTitle("抢单订单详情");
        //点导航栏图标退出
        tbgoback.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView tvorder_oid= (TextView) findViewById(R.id.order_oid_Grab);
        tvorder_oid.setText("订单编号："+orderBean.getOid());
        TextView tvorder_type= (TextView) findViewById(R.id.order_type_Grab);
        if(orderBean.getServiceclass().equals("0001000300010001")) {
            tvorder_type.setText("订单类型：日常保洁");
        }
        TextView tvservice_time= (TextView) findViewById(R.id.service_time_Grab);
        tvservice_time.setText("服务时间："+orderBean.getOrderAgreedTime());
        TextView tvreservation_number= (TextView) findViewById(R.id.reservation_number_Grab);
        tvreservation_number.setText("预约数量："+orderBean.getOrdernum());
        TextView tvservice_site= (TextView) findViewById(R.id.service_site_Grab);
        tvservice_site.setText("服务地点："+orderBean.getAddressArea());
        TextView tvlinear_distance= (TextView) findViewById(R.id.linear_distance_Grab);
        tvlinear_distance.setText("直线距离："+orderBean.getConfirmDistance());
        TextView tvremark= (TextView) findViewById(R.id.remark_Grab);
        tvremark.setText("备注:"+orderBean.getCustomerRemark());
        EditText edservice_number= (EditText) findViewById(R.id.service_number_Grab);
        final String number=edservice_number.getText().toString();
        Button btnsubmit= (Button) findViewById(R.id.btn_submit_Grab);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetWorkTools.requestGrab_Single(orderBean.getOrderno()+ "", number, MyApplication.userBean.getUserId()+"", new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Bean bean=JSON.parseObject(result,Bean.class);
                        if (bean.getCode().equals("200")){
                            Toast.makeText(getApplicationContext(),"抢单成功！",Toast.LENGTH_SHORT).show();
                            finish();
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
            }
        });
    }
}
