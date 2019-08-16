package com.extop.education.Activity;


import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.extop.education.Adapter.NetWorkTools;
import com.extop.education.MyApplication;
import com.extop.education.R;
import com.extop.education.wxapi.Constants;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xutils.common.Callback;
import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkCookieManager;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import static com.extop.education.Adapter.SoftHideKeyBoardUtil.assistActivity;

/**
 * Created by Administrator on 2017/11/23.
 * 电子商务
 */

public class ElectronicCommerceActivity extends AppCompatActivity implements View.OnClickListener {
    //菜单栏
    TextView tv_video, tv_frequency, tv_pdf, tv_Image, tv_title;
    //标题栏
    Toolbar tb_toolbar;
    //webView
    XWalkView wv_VideoDetails;
    Intent intent;
    //按钮的类型
    String type = "订单";//Cookie="";目前废弃
    //微信API
    private IWXAPI api;
    private SendMessageToWX.Req req;
    private Bitmap bitmapShare;
    //悬浮框
    private PopupWindow mPopWindow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Window window = getWindow();
        NetWorkTools.taskbar_transparent(window, this);


        //取消标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_inside_search);
        //键盘弹起窗口可拉伸
        assistActivity(this);
        //获取控件
        tb_toolbar = (Toolbar) findViewById(R.id.video_icon);
        tv_title = (TextView) findViewById(R.id.video_title);
        tv_video = (TextView) findViewById(R.id.tv_video);
        tv_Image = (TextView) findViewById(R.id.tv_Image);
        tv_frequency = (TextView) findViewById(R.id.tv_frequency);
        tv_pdf = (TextView) findViewById(R.id.tv_pdf);
        wv_VideoDetails = (XWalkView) findViewById(R.id.wv_VideoDetails);

        //目前图片和音频不菜单不启用
        tv_Image.setVisibility(View.GONE);
        tv_frequency.setVisibility(View.GONE);
        intent = getIntent();
        //设置标题、设置返回按钮并添加监听事件
        tv_title.setText("电子商务");
        tv_video.setText("订单");
        tv_pdf.setText("收藏");

        //得到XWalkView的Cookie；
        /*XWalkCookieManager xm = new XWalkCookieManager();
        xm.setAcceptCookie(true);
        Cookie=xm.getCookie(getPage(type));*/

        tb_toolbar.setNavigationIcon(R.mipmap.arrow_left_d);
        tb_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //默认选中视频  给视频、音频、文案、图片绑定单击事件
        tv_video.setSelected(true);
        tv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_video.setSelected(true);
                type = "订单";
                wv_VideoDetails.loadUrl(getPage(type));
                Log.v("url", getPage(type));
            }
        });
        tv_frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_frequency.setSelected(true);
                type = "mp3";
                wv_VideoDetails.loadUrl(getPage(type));
                Log.v("url", getPage(type));
            }
        });
        tv_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_Image.setSelected(true);
                type = "png";
                wv_VideoDetails.loadUrl(getPage(type));
                Log.v("url", getPage(type));
            }
        });
        tv_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected();
                tv_pdf.setSelected(true);
                type = "收藏";
                wv_VideoDetails.loadUrl(getPage(type));
                Log.v("url", getPage(type));
            }
        });

        //调用IWXMsg.registerApp将应用注册到微信
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);
        //webView设置属性
        NetWorkTools.XWalkView_Settings(wv_VideoDetails, this);
        wv_VideoDetails.setUIClient(new XWalkUIClient(wv_VideoDetails) {
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                Toast.makeText(ElectronicCommerceActivity.this,message,Toast.LENGTH_SHORT).show();
                result.cancel();
                return true;
            }
        });
        wv_VideoDetails.loadUrl(getPage(type));
    }


    //得到当前按钮被选中的URL地址
    private String getPage(String type) {
        String page = "";
        switch (type) {
            case "订单":
                page = MyApplication.url + "phone_order_list.view";
                break;
            case "收藏":
                page = MyApplication.url + "collect_product.view";
                break;
        }
        return page;
    }


    //重置所有文本的选中状态
    private void setSelected() {
        tv_video.setSelected(false);
        tv_frequency.setSelected(false);
        tv_pdf.setSelected(false);
        tv_Image.setSelected(false);
    }

    //跳转订单详情或商品详情
    @JavascriptInterface
    public void setValue(String[] string) {
        Log.d("订单id", string[0]);
        Log.d("订单状态", string[1]);
        switch (type){
            case "订单":
                intent=new Intent("android.intent.Activity.ProductOrderDetailsActivity");
                intent.putExtra("orderId",string[0]);
                intent.putExtra("orderStatus", string[1]);
                break;
            case "收藏":
                intent = new Intent("android.intent.Activity.ProductDetailsActivity");
                intent.putExtra("ProductId", string[0]);
                break;
        }
        startActivity(intent);
    }

    //确认收货
    @JavascriptInterface
    public void ConfirmGoods(String[] number){
        Log.d("订单ID", number[0]);
        Log.d("点击的第几个", number[1]);
        dialog(this,number[0],number[1]);
    }

    //消息对话框
    public  void dialog(AppCompatActivity activity, final String orderId,final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialog);
        builder.setMessage("请收到货后，再确认收货！否则您可能钱货两空！");
        builder.setPositiveButton("确认收货", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                NetWorkTools.request(orderId,new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("测试", NetWorkTools.replaceBlank(result));
                        //webView在另外的一个线程中使用loadUrl 调用搜索圈子方法
                        wv_VideoDetails.post(new Runnable() {
                            @Override
                            public void run() {
                                wv_VideoDetails.loadUrl("javascript:Modify_order_status('" + message+ "')");
                            }
                        });
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

    //商家信息
    @JavascriptInterface
    public void businessInformation(String[] valueArray) {
        Log.d("商家信息", valueArray[0]);
        NetWorkTools.dialog(ElectronicCommerceActivity.this,valueArray[0]);
    }

    //得到微信分享需要的 0:title（标题），1:content（描述），2:id
    @JavascriptInterface
    public void weChatShare(String[] shareArray) {
        Log.d("标题", shareArray[0]);
        Log.d("内容", NetWorkTools.replaceBlank(shareArray[1]));
        Log.d("id", shareArray[2]);
        Log.d("图片", shareArray[3]);
        //初始化一个WXWebPageObject对象，填写URL
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = MyApplication.url + "wx_share_merchandise.view?id=" + shareArray[2];
        Log.d("商品微信分享地址", webPage.webpageUrl);
        //用WXWebPageObject对象初始化一个WXMediaMessage对象，填写标题、描述
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = shareArray[0];
        msg.description = NetWorkTools.replaceBlank(shareArray[1]);
//        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
//        Bitmap bitmap=getURLImage(MyApplication.url + "upload/" + MyApplication.circleIcon);
        //微信将bitmap转换成byte[]
//        msg.thumbData=Util.bmpToByteArray(bitmap,true);
        //将bitmap转换成byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String imageType = shareArray[3].substring(shareArray[3].length() - 3);

        //判断图片类型 根据类型进行图片压缩 png或者jpg分享用圈子图片其他类型的用KMoon
        if (imageType.equals("png")) {
            bitmapShare = NetWorkTools.getURLImage(MyApplication.url + "upload/" + shareArray[3]);
            //判断图片大小如果大于32kb进行最大模糊处理
            if (bitmapShare.getByteCount() > 32768) {
                bitmapShare.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
            } else {
                bitmapShare.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            }
        } else if (imageType.equals("jpg") || imageType.equals("jpeg")) {
            bitmapShare = NetWorkTools.getURLImage(MyApplication.url + "upload/" + shareArray[3]);
            if (bitmapShare.getByteCount() > 32768) {
                bitmapShare.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
            } else {
                bitmapShare.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            }

        } else {
            Log.d("其他类型", MyApplication.url + "upload/" + MyApplication.circleIcon);
            bitmapShare = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
            bitmapShare.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        }

        msg.thumbData = byteArrayOutputStream.toByteArray();
        //构造一个Rep
        req = new SendMessageToWX.Req();
        req.message = msg;
        showPopupWindow();
    }

    //用户点击购买调到购买详情页面 0:商品名称 1:商品单价 2:商品ID 3:商品库存
    @JavascriptInterface
    public void buy(String[] buyArray) {
        Log.d("ProductId", buyArray[0]);
        intent = new Intent("android.intent.Activity.ProductOrderActivity");
        intent.putExtra("ProductId", buyArray[0]);
        startActivity(intent);
    }

    //显示PopupWindow
    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(ElectronicCommerceActivity.this).inflate(R.layout.popupwindow_layout, null);
        mPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        TextView pop_SceneSession = (TextView) contentView.findViewById(R.id.pop_SceneSession);
        TextView pop_SceneTimeline = (TextView) contentView.findViewById(R.id.pop_SceneTimeline);
        TextView pop_cancel = (TextView) contentView.findViewById(R.id.pop_cancel);
        pop_SceneSession.setOnClickListener(this);
        pop_SceneTimeline.setOnClickListener(this);
        pop_cancel.setOnClickListener(this);
        //显示PopupWindow
        View rootView = LayoutInflater.from(ElectronicCommerceActivity.this).inflate(R.layout.popupwindow_layout, null);
        mPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    //处理PopupWindow每一项的点击事件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.pop_SceneSession: {
                //分享到好友会话
                req.scene = SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);
                mPopWindow.dismiss();
            }
            break;
            case R.id.pop_SceneTimeline: {
                //        分享到朋友圈
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
                mPopWindow.dismiss();
            }
            break;
            case R.id.pop_cancel: {
                mPopWindow.dismiss();
            }
            break;
        }
    }

    //用户点返回键时直接关闭当前活动
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*View.VISIBLE 常量值为0，意思是可见的
        *View.INVISIBLE 常量值为4，意思是不可见的
        * View.GONE 常量值为8，意思是不可见的，而且不占用布局空间
        * */
        wv_VideoDetails.getNavigationHistory().clear();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

}
