package com.extop.education.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 * 自定义SharedHelper读取本地文件
 */

public class SharedHelper {

    private Context mContext;

    public SharedHelper() {
    }

    public SharedHelper(Context mContext) {
        this.mContext = mContext;
    }


    //定义一个保存数据的方法
    public void save(String userName, String passWord) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName", userName);
        editor.putString("passWord", passWord);
        editor.commit();
//        Toast.makeText(mContext, "登录成功！", Toast.LENGTH_SHORT).show();
        Log.d("本地记录","账号密码保存成功");
    }
    //定义一个保存开启无图的方法
    public void setNoImages(Boolean NoImages) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("NoImages", NoImages);
        editor.commit();
//        Toast.makeText(mContext, "存入成功", Toast.LENGTH_SHORT).show();
    }
    //保存圈子列表是否开启九宫格
    public void setIsCircleJGG(Boolean isCircleJGG) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isCircleJGG", isCircleJGG);
        editor.commit();
//        Toast.makeText(mContext, "存入成功", Toast.LENGTH_SHORT).show();
    }
    //保存圈子详情是否开启九宫格
    public void setIsCircleDetailsJGG(Boolean isCircleDetailsJGG) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isCircleDetailsJGG", isCircleDetailsJGG);
        editor.commit();
//        Toast.makeText(mContext, "存入成功"+isCircleDetailsJGG, Toast.LENGTH_SHORT).show();
    }
    //保存签署意见
    public void setSaveOpinion(String opinion) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("opinion", opinion);
        editor.commit();
        Toast.makeText(mContext, "存入成功"+opinion, Toast.LENGTH_SHORT).show();
    }
    //定义一个读取SP文件的方法
    public Map<String, Object> read() {
        Map<String, Object> data = new HashMap<String, Object>();
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        data.put("userName", sp.getString("userName", ""));
        data.put("passWord", sp.getString("passWord", ""));
        data.put("isCircleJGG",sp.getBoolean("isCircleJGG",true));
        data.put("isCircleDetailsJGG",sp.getBoolean("isCircleDetailsJGG",true));
        data.put("opinion",sp.getString("opinion",""));
        data.put("NoImages",sp.getBoolean("NoImages",false));
        return data;
    }
}
