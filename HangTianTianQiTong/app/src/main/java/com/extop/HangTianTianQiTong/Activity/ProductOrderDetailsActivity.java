package com.extop.HangTianTianQiTong.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.extop.HangTianTianQiTong.Adapter.NetWorkTools;
import com.extop.HangTianTianQiTong.Adapter.PayResult;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.wxapi.Constants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xwalk.core.JavascriptInterface;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/24.
 */

public class ProductOrderDetailsActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "phone_order_details.view?id=",orderStatus="",orderId="";

    /*支付宝支付需要的变量和线程*/
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        finish();
                        Toast.makeText(ProductOrderDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ProductOrderDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("订单详情");
        orderStatus=intent.getStringExtra("orderStatus");
        orderId=intent.getStringExtra("orderId");
        switch (orderStatus){
            case "未付款":
                url= MyApplication.url + "phone_order_payment.view?&id="+orderId;
                break;
            default:
                url = MyApplication.url + "phone_order_details.view?id="+orderId;
                break;
        }
        xWalkView.loadUrl(url);
        Log.d("d订单详情地址", url);
        //调用IWXMsg.registerApp将应用注册到微信
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);
    }
    @JavascriptInterface
    public void setValue(String[] valueArray) {
        Log.d("支付类型", valueArray[0]);
        Log.d("订单id", valueArray[1]);
        Log.d("总金额", valueArray[2]);
        if (valueArray[0].equals("支付宝")) {
            NetWorkTools.request(valueArray[1],valueArray[2], new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.d("测试", NetWorkTools.replaceBlank(result));
                    final String orderInfo = NetWorkTools.replaceBlank(result);   // 订单信息

                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(ProductOrderDetailsActivity.this);

                            Map<String, String> result = alipay.payV2(orderInfo, true);
                            Log.i("msp", result.toString());

                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.d("错误", ex + "");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            NetWorkTools.request_wx_merchandise(valueArray[1],valueArray[2], new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.d("订单", NetWorkTools.replaceBlank(result));

                    try {
                        JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                        if (null != json && !json.has("retcode")) {
                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = json.getString("appid");
                            req.partnerId = json.getString("partnerid");
                            req.prepayId = json.getString("prepayid");
                            req.nonceStr = json.getString("noncestr");
                            req.timeStamp = json.getString("timestamp");
                            req.packageValue = json.getString("package");
                            req.sign = json.getString("sign");
                            req.extData = "app data"; // optional
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.registerApp(Constants.APP_ID);
                            api.sendReq(req);
                        } else {
                            Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                        }
                    } catch (Exception e) {
                        Log.e("PAY_GET", "异常：" + e.getMessage());
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.d("cw", ex + "");
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
}
