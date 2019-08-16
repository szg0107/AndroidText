package com.extop.HangTianTianQiTong.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.extop.HangTianTianQiTong.MyApplication;
import com.extop.HangTianTianQiTong.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/17.
 */

public class NetWorkTools {
    public static String url = "http://192.168.1.101:8080/apptest/servlet";

    //一、登录获取USER方法
    //用户名，密码，Callback
    public static void requestLog(String name, String pwd, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(url + "/Login");
        params.addBodyParameter("phoneNumber", name);//普通post
        params.addBodyParameter("password", pwd);//普通post
        x.http().post(params, callback);
    }


    //圈子收费支付宝调用jsp获得订单信息   圈子id、用户id、订单描述
    public static void request(String circleId, String userId, String Subject, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(MyApplication.url + "res/java/AlipayConfig.jsp");
        params.addBodyParameter("circleId", circleId);//普通post
        params.addBodyParameter("userId", userId);//普通post
        params.addBodyParameter("Subject", Subject);//普通post
        x.http().post(params, callback);
    }

    //支付宝任务收费  圈子id、任务id、用户id、订单描述、自愿金额
    public static void request(String circleId, String TaskID, String userId, String Subject, String money, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(MyApplication.url + "res/java/pay_the_fees.jsp");
        params.addBodyParameter("circleId", circleId);
        params.addBodyParameter("TaskID", TaskID);//普通post
        params.addBodyParameter("userId", userId);//普通post
        params.addBodyParameter("Subject", Subject);//普通post
        params.addBodyParameter("money", money);
        x.http().post(params, callback);
    }

    //支付宝商品收费  订单id、金额
    public static void request(String orderId, String money, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(MyApplication.url + "res/java/zfb_spzf.jsp");
        params.addBodyParameter("id", orderId);//普通post
        params.addBodyParameter("money", money);
        x.http().post(params, callback);
    }

    //支付宝商品确认收货转正到商家  订单id
    public static void request(String orderId,Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(MyApplication.url + "res/java/zfb_sjzc.jsp");
        //添加请求头
//        params.addHeader("Cookie",Cookie);
        params.addBodyParameter("id", orderId);//普通post
        x.http().post(params, callback);
    }

    //圈子收费微信调用jsp获得订单信息   圈子id、用户id、订单描述
    public static void request_wx(String circleId, String userId, String Subject, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(MyApplication.url + "res/java/wx_pay.jsp");
        params.addBodyParameter("circleId", circleId);//普通post
        params.addBodyParameter("userId", userId);//普通post
        params.addBodyParameter("Subject", Subject);//普通post
        x.http().post(params, callback);
    }

    //微信任务收费  圈子id、任务id、用户id、订单描述、自愿金额
    public static void request_wx_task(String circleId, String TaskID, String userId, String Subject, String money, Callback.CommonCallback<String> callback) {
        Log.e("参数", "circleId=====" + circleId + ",TaskID====" + TaskID + ",userId====" + userId + ",Subject====" + Subject + ",money====" + money);
        RequestParams params = new RequestParams(MyApplication.url + "res/java/wx_task_pay.jsp");
        params.addBodyParameter("circleId", circleId);
        params.addBodyParameter("TaskID", TaskID);//普通post
        params.addBodyParameter("userId", userId);//普通post
        params.addBodyParameter("Subject", Subject);//普通post
        params.addBodyParameter("money", money);
        x.http().post(params, callback);
    }

    //微信商品收费  订单id、金额
    public static void request_wx_merchandise(String orderId, String money, Callback.CommonCallback<String> callback) {
        Log.e("参数", "orderId=====" + orderId +",money====" + money);
        RequestParams params = new RequestParams(MyApplication.url + "res/java/wx_spzf.jsp");
        params.addBodyParameter("id", orderId);//普通post
        params.addBodyParameter("money", money);
        x.http().post(params, callback);
    }

    //得到音频封面图
    public static void getAudioImage(String audio_name,Callback.CommonCallback<String> callback){
        RequestParams params = new RequestParams(MyApplication.url + "res/java/audio_cover_plan.jsp");
        params.addBodyParameter("audio_name", audio_name);//普通post
        x.http().post(params, callback);
    }

    //状态栏设置颜色
    public static void taskbar_transparent(Window window, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (android.os.Build.BRAND.equals("Huawei")) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setTintColor(Color.parseColor("#029AE5"));
        }

    }

    //XWalkView设置常用属性
    public static void XWalkView_Settings(XWalkView webView, final AppCompatActivity activity) {
        webView.setResourceClient(new XWalkResourceClient(webView) {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setUIClient(new XWalkUIClient(webView) {
            //处理alert弹出框
            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                result.cancel();
                return true;
            }
        });
        //设置自适应屏幕，两者合用
        webView.getSettings().setUseWideViewPort(true);//将图片调整到适合webView的大小
        webView.getSettings().setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webView.loadUrl("javascript:document.body.contentEditable=true;");//设置WebView属性,运行执行js脚本
        webView.addJavascriptInterface(activity, "callByJs");
    }

    //消息对话框
    public static void dialog(AppCompatActivity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialog);
        builder.setMessage(message);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    //获取融云Token
    public static void getRongCloudToken(String userName, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(MyApplication.url + "res/java/GetRongCloudToken.jsp");
        params.addBodyParameter("userId", userName);//普通post
        x.http().post(params, callback);
    }

    //获取融云用户信息提供者
    public static void getUserInformationProvider(String userName, Callback.CommonCallback<String> callback) {
        RequestParams params = new RequestParams(MyApplication.url + "res/java/getUserInformation.jsp");
        params.addBodyParameter("userId", userName);//普通post
        x.http().post(params, callback);
    }

    //java去除字符串中的空格、回车、换行符、制表符
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    //将图片URL转换为Bitmap
    public static Bitmap getURLImage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

}
