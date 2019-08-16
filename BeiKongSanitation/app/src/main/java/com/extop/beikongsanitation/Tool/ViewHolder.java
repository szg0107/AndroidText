package com.extop.beikongsanitation.Tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/12.
 */

public class ViewHolder {
    //自定义ViewHolder
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        //context 上下文
        //初始化 mPosition和SparseArray
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }
    //入口方法判断ViewHolder是否需要new
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            //取出ViewHolder
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    //通过viewId获取控件
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    //给TextView赋值
    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
    //给ImageView设置Resource
    public ViewHolder setImageResource(int viewId, int resId){
        ImageView view= getView(viewId);
        view.setImageResource(resId);
        return this;
    }
    //给ImageView设置Bitamp
    public ViewHolder setImageBitamp(int viewId, Bitmap bitmap){
        ImageView view= getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }
    //给ImageView设置网络图片
    public ViewHolder setImageURL(int viewId, String url){
        ImageView view= getView(viewId);
        //Imageloader.getInstance().loadImg(view,url)
        return this;
    }
}
