package com.szg.homemakingapplication.accesstool;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */

public class SharedHelper {

    private Context mContext;

    public SharedHelper() {
    }

    public SharedHelper(Context mContext) {
        this.mContext = mContext;
    }


    //定义一个保存数据的方法
    public void save(String username, String passwd,boolean RememberPwd) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.putString("passwd", passwd);
        editor.putBoolean("RememberPwd",RememberPwd);
        editor.commit();
        Toast.makeText(mContext, "登录成功！", Toast.LENGTH_SHORT).show();
    }

    //定义一个读取SP文件的方法
    public Map<String, Object> read() {
        Map<String, Object> data = new HashMap<String, Object>();
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        data.put("username", sp.getString("username", ""));
        data.put("passwd", sp.getString("passwd", ""));
        data.put("AutoLogin",sp.getBoolean("AutoLogin",false));
        data.put("RememberPwd",sp.getBoolean("RememberPwd",false));
        data.put("is_rest",sp.getBoolean("is_rest",true));
        return data;
    }
}
