package com.szg.homemakingapplication.UI.orderActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.szg.homemakingapplication.accesstool.SharedHelper;
import com.szg.homemakingapplication.location.LocationService;
import com.szg.homemakingapplication.model.Bean;
import com.szg.homemakingapplication.model.OrderBean;

import org.xutils.common.Callback;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/3.
 */

public class Submit_service_centent extends AppCompatActivity {
    private OrderBean orderBean;
    private int i = 1, j;
    private Button btn_reduce, btn_add, btn_submit;
    private EditText etservice_number;
    private TextView tvtotal_prices;
    private Toolbar tbgoback;
    private SharedHelper sharedHelper;
    private LocationService locationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.payment_for_orders_centent);
        Intent intent = getIntent();
        tbgoback = (Toolbar) findViewById(R.id.toolbar_obligation);
        tbgoback.setNavigationIcon(R.drawable.arrow_left_d);
        tbgoback.setTitle("提交服务数量");
        //点导航栏图标退出
        tbgoback.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        orderBean = (OrderBean) intent.getSerializableExtra("orderBeens");
        TextView tvorder_number = (TextView) findViewById(R.id.order_number_obligation);
        tvorder_number.setText("订单编号：" + orderBean.getOrderno());
        TextView tvorder_status = (TextView) findViewById(R.id.order_status_obligation);
        tvorder_status.setText("订单状态：代付款！");
        TextView tvorder_type = (TextView) findViewById(R.id.order_typ_obligation);
        if (orderBean.getServiceclass().equals("0001000300010001")) {
            tvorder_type.setText("订单类型：日常保洁");
        }
        TextView tvservice_time = (TextView) findViewById(R.id.service_time_obligation);
        tvservice_time.setText("服务时间：" + orderBean.getOrderAgreedTime());
        TextView tvreservation_number = (TextView) findViewById(R.id.reservation_number_obligation);
        tvreservation_number.setText("预约数量：" + orderBean.getOrdernum());
        TextView tvservice_site = (TextView) findViewById(R.id.service_site_obligation);
        tvservice_site.setText("服务地点：" + orderBean.getAddressArea());
        TextView tvremark = (TextView) findViewById(R.id.remark_obligation);
        tvremark.setText("备注:" + orderBean.getCustomerRemark());
        TextView tvunit_price = (TextView) findViewById(R.id.unit_price_obligation);
        tvunit_price.setText("单价：" + orderBean.getOrderprice());
        tvtotal_prices = (TextView) findViewById(R.id.total_prices_obligation);
        btn_reduce = (Button) findViewById(R.id.reduce_obligation);
        etservice_number = (EditText) findViewById(R.id.service_number_obligation);
        btn_add = (Button) findViewById(R.id.btn_add_obligation);
        btn_submit = (Button) findViewById(R.id.btn_submit_obligation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EditText内容发生改变时监听事件 改变前 改变中 改变后
        etservice_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0 && j < 100) {
                    j = Integer.parseInt(editable.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "请输入1~100以内整数！", Toast.LENGTH_SHORT).show();
                    j = 0;
                }
                if (i != j) {
                    i = j;
                    tvtotal_prices.setText("总价:" + (i * orderBean.getOrderprice()));
                }
            }
        });
        btn_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i > 1) {
                    i--;
                    etservice_number.setText(i + "");
                    tvtotal_prices.setText("总价:" + (i * orderBean.getOrderprice()));
                }
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                etservice_number.setText(i + "");
                tvtotal_prices.setText("总价:" + (i * orderBean.getOrderprice()));
            }
        });
        tvtotal_prices.setText("总价:" + (i * orderBean.getOrderprice()));
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedHelper = new SharedHelper(getApplication());
                Map<String, Object> data = sharedHelper.read();
                if (data.get("is_rest").toString().equals("true")) {
                    Start_location_services();
                    NetWorkTools.requestSubmit_the_payment_information(orderBean.getOrderno() + "", etservice_number.getText().toString(), MyApplication.locationBean.getBaiduMapLat(), MyApplication.locationBean.getBaiduMapLng(), new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Bean bean = JSON.parseObject(result, Bean.class);
                            if (bean.getCode().equals("200")) {
                                Toast.makeText(getApplicationContext(), "提交成功！", Toast.LENGTH_SHORT).show();
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
                    locationService.stop();
                } else {
                    NetWorkTools.requestSubmit_the_payment_information(orderBean.getOrderno() + "", etservice_number.getText().toString(), MyApplication.locationBean.getBaiduMapLat(), MyApplication.locationBean.getBaiduMapLng(), new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Bean bean = JSON.parseObject(result, Bean.class);
                            if (bean.getCode().equals("200")) {
                                Toast.makeText(getApplicationContext(), "提交成功！", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    protected void Start_location_services() {
        locationService = MyApplication.locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(NetWorkTools.mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }
}
