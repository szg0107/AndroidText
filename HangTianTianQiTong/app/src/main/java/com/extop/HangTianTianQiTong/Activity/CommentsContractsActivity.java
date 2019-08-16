package com.extop.HangTianTianQiTong.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.extop.HangTianTianQiTong.Adapter.SharedHelper;
import com.extop.HangTianTianQiTong.Adapter.ToolbarWebViewActivity;
import com.extop.HangTianTianQiTong.MyApplication;
import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;
import java.util.Map;

/**
 * Created by szg on 2018/3/6.
 * 签署意见
 */

public class CommentsContractsActivity extends ToolbarWebViewActivity {
    private String url = MyApplication.url + "commentsContracts.view?&taskId=",opinion="";
    //读取本地文件
    private SharedHelper sharedHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tw_title.setText("签署意见");
        //获取当前时间戳
//        Date d = new Date();
//        long timestamp=d.getTime();
//        Log.d("时间戳",timestamp+"");
        xWalkView.setResourceClient(new XWalkResourceClient(xWalkView) {
            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);
                if (opinion != "" && opinion != null) {
                    xWalkView.loadUrl("javascript:setTextAreaValue('" + opinion + "')");
                }
            }
        });
        xWalkView.loadUrl(url + intent.getStringExtra("taskID")+"&transmittalMode="+intent.getStringExtra("transmittalMode")+"&circleId="+MyApplication.circleID);//调用loadView方法为WebView加入链接
        Log.d("url", url + intent.getStringExtra("taskID")+"&transmittalMode="+intent.getStringExtra("transmittalMode"+"&circleId="+MyApplication.circleID));
        readSharedHelper();
    }
    //保存意见
    @JavascriptInterface
    public void saveOpinion(String string) {
        if (string.equals("提交")){
            finish();
        }else{
            sharedHelper.setSaveOpinion(string);
        }
    }
    //读取本地文件
    public void readSharedHelper() {
        sharedHelper = new SharedHelper(getApplicationContext());
        Map<String, Object> data = sharedHelper.read();
        opinion=data.get("opinion").toString();
    }
}
