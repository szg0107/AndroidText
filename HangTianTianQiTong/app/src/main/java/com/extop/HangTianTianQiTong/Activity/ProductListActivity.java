package com.extop.HangTianTianQiTong.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.extop.HangTianTianQiTong.Adapter.NetWorkTools;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;
import com.extop.HangTianTianQiTong.wxapi.Constants;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xwalk.core.JavascriptInterface;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/11/22.
 * 商品列表
 */

public class ProductListActivity extends ToolbarWebViewActivity implements View.OnClickListener {
    private String url = MyApplication.url + "product_list.view?id=" + MyApplication.circleID;
    //微信API
    private IWXAPI api;
    private SendMessageToWX.Req req;
    private Bitmap bitmapShare;
    //悬浮框
    private PopupWindow mPopWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //调用IWXMsg.registerApp将应用注册到微信
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);
        xWalkView.loadUrl(url);
        Log.d("电子商务地址", url);
    }
    //进入商品详情
    @JavascriptInterface
    public void setValue(String[] valueArray) {
        Log.d("商品id", valueArray[0]);
        intent = new Intent("android.intent.Activity.ProductDetailsActivity");
        intent.putExtra("ProductId", valueArray[0]);
        startActivity(intent);
    }

    //商家信息
    @JavascriptInterface
    public void businessInformation(String[] valueArray) {
        Log.d("商家信息", valueArray[0]);
        NetWorkTools.dialog(ProductListActivity.this,valueArray[0]);
    }

    //得到微信分享需要的 0:title（标题），1:content（描述），2:id，3:圈子图片
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
        Log.d("id", buyArray[0]);
        intent = new Intent("android.intent.Activity.ProductOrderActivity");
        intent.putExtra("ProductId",buyArray[0]);
        startActivity(intent);
    }

    //显示PopupWindow
    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(ProductListActivity.this).inflate(R.layout.popupwindow_layout, null);
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
        View rootView = LayoutInflater.from(ProductListActivity.this).inflate(R.layout.popupwindow_layout, null);
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
}
