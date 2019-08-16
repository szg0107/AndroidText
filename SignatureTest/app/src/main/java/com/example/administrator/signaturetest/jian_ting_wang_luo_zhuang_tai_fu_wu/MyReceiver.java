package com.example.administrator.signaturetest.jian_ting_wang_luo_zhuang_tai_fu_wu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/27.
 * 网络状态服务
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";
    private BRInteraction brInteraction;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, intent+"");
        if (!isNetworkAvailable(context)) {
            Toast.makeText(context, "请检查您的网络", Toast.LENGTH_SHORT).show();
            brInteraction.setText(false);
        }else {
            Toast.makeText(context, "有网络", Toast.LENGTH_SHORT).show();
            brInteraction.setText(true);
        }
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
    public interface BRInteraction {
        public void setText(Boolean content);
    }

    public void setBRInteractionListener(BRInteraction brInteraction) {
        this.brInteraction = brInteraction;
    }
}
