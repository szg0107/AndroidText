package com.extop.education.Adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by Administrator on 2016/12/12.
 */

public class ViewHolder {
    //自定义ViewHolder
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertview;

    public ViewHolder(Context context, ViewGroup parent, int latoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertview = LayoutInflater.from(context).inflate(latoutId, parent, false);
        mConvertview.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int latoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, latoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    //通过viewId获取控件
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertview.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertview() {
        return mConvertview;
    }

    //给TextView赋值
    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
    //给ImageView赋值
    public ViewHolder setImageResource(int viewId, int resId){
        ImageView view= getView(viewId);
        view.setImageResource(resId);
        return this;
    }
    public ViewHolder setImageURL(int viewId, String url){
        ImageView view= getView(viewId);
        //Imageloader.getInstance().loadImg(view,url)
        return this;
    }
}
