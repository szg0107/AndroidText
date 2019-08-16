package com.szg.homemakingapplication.UI.orderActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.szg.homemakingapplication.R;
import com.szg.homemakingapplication.model.OrderBean;

/**
 * Created by Administrator on 2016/9/5.
 */

public class History_order_centen extends AppCompatActivity {
    private OrderBean orderBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history_order_centen);
        Intent intent = getIntent();
        Toolbar tbgoback= (Toolbar) findViewById(R.id.toolbar_history);
        tbgoback.setNavigationIcon(R.drawable.arrow_left_d);
        tbgoback.setTitle("已完成订单详情");
        //点导航栏图标退出
        tbgoback.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        orderBean = (OrderBean) intent.getSerializableExtra("orderBeens");
        TextView tvorder_number = (TextView) findViewById(R.id.order_number_history);
        tvorder_number.setText("订单编号：" + orderBean.getOrderno());
        TextView tvorder_status = (TextView) findViewById(R.id.order_status_history);
        switch (orderBean.getState()){
            case 5:
                tvorder_status.setText("订单状态：待支付");
                break;
            case 6:
                tvorder_status.setText("订单状态：已完成");
                break;
        }
        TextView tvorder_type = (TextView) findViewById(R.id.order_typ_history);
        if(orderBean.getServiceclass().equals("0001000300010001")) {
            tvorder_type.setText("订单类型：日常保洁");
        }
        TextView tvservice_time = (TextView) findViewById(R.id.service_time_history);
        tvservice_time.setText("服务时间：" + orderBean.getOrderAgreedTime());
        TextView tvreservation_number = (TextView) findViewById(R.id.reservation_number_history);
        tvreservation_number.setText("预约数量：" + orderBean.getOrdernum());
        TextView tvservice_site = (TextView) findViewById(R.id.service_site_history);
        tvservice_site.setText("服务地点：" + orderBean.getAddressArea());
        TextView tvremark = (TextView) findViewById(R.id.remark_history);
        tvremark.setText("消费者：" + orderBean.getLinkman());
        TextView tvtotal_prices = (TextView) findViewById(R.id.total_prices_history);
        tvtotal_prices.setText("订单总价："+orderBean.getPayAmount());
    }
}
