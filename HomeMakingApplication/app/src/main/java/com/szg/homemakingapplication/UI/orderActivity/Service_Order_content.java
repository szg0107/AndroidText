package com.szg.homemakingapplication.UI.orderActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.model.OrderBean;

/**
 * Created by Administrator on 2016/9/1.
 */

public class Service_Order_content extends AppCompatActivity {
    Button btnCheck_the_wiring,btnContact_clients;
    private OrderBean orderBean;
    private Toolbar tbgoback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.service_order_content_layout);
        Intent intent=getIntent();
        tbgoback= (Toolbar) findViewById(R.id.toolbar_Forservice);
        tbgoback.setNavigationIcon(R.drawable.arrow_left_d);
        tbgoback.setTitle("待服务订单详情");
        //点导航栏图标退出
        tbgoback.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tbgoback.inflateMenu(R.menu.service_order_toobar_menu_submit);
        orderBean= (OrderBean) intent.getSerializableExtra("orderBeens");
        TextView tvorder_number= (TextView) findViewById(R.id.order_number_Forservice);
        tvorder_number.setText("订单编号："+orderBean.getOrderno());
        TextView tvorder_status= (TextView) findViewById(R.id.order_status_Forservice);
        tvorder_status.setText("订单状态：待服务！");
        TextView tvorder_type= (TextView) findViewById(R.id.order_type_Forservice);
        if(orderBean.getServiceclass().equals("0001000300010001")) {
            tvorder_type.setText("订单类型：日常保洁");
        }
        TextView tvservice_time= (TextView) findViewById(R.id.service_time_Forservice);
        tvservice_time.setText("服务时间："+orderBean.getOrderAgreedTime());
        TextView tvreservation_number= (TextView) findViewById(R.id.reservation_number_Forservice);
        tvreservation_number.setText("预约数量："+orderBean.getOrdernum());
        TextView tvservice_site= (TextView) findViewById(R.id.service_site_Forservice);
        tvservice_site.setText("服务地点："+orderBean.getAddressArea());
        TextView tvremark= (TextView) findViewById(R.id.remark_Forservice);
        tvremark.setText("备注:"+orderBean.getCustomerRemark());
        TextView tvunit_price= (TextView) findViewById(R.id.unit_price_Forservice);
        tvunit_price.setText("订单价格："+orderBean.getOrderprice());
        btnCheck_the_wiring= (Button) findViewById(R.id.Check_the_wiring_Forservice);
        btnContact_clients= (Button) findViewById(R.id.Contact_clients_Forservice);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tbgoback.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.putExtra("orderBeens",orderBean);
                intent.setAction("android.intent.szg.Submit_Service_Centent");
                startActivity(intent);
                return true;
            }
        });
        btnCheck_the_wiring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction("android.intent.maps.RoutePlanDemo");
                startActivity(intent);
            }
        });
        btnContact_clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" +orderBean.getContactMobile());
                intent.setData(data);
                startActivity(intent);
            }
        });
    }
}
