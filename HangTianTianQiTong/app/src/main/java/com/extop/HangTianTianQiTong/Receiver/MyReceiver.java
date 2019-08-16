package com.extop.HangTianTianQiTong.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Administrator on 2017/5/27.
 * 网络监听服务
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";
    private BRInteraction brInteraction;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, intent+"");
        if (!isNetworkAvailable(context)) {
            brInteraction.setIsNet(false);
        }else {
            brInteraction.setIsNet(true);
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
        public void setIsNet(Boolean content);
    }

    public void setBRInteractionListener(BRInteraction brInteraction) {
        this.brInteraction = brInteraction;
    }
}
