package com.extop.beikongsanitation.Tool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    //自定义万能适配器
    protected Context mContext;
    //数据集
    protected List<T> mDatas;
    //加载布局
    protected LayoutInflater mInflater;
    //布局文件
    private int layoutId;
    protected int mPosition;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        mInflater= LayoutInflater.from(context);
        this.mDatas=datas;
        this.layoutId=layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        ViewHolder holder=ViewHolder.get(mContext,view,viewGroup, layoutId,i);
        mPosition=i;
        convert(holder,getItem(i));
        return holder.getConvertView();
    }
    public abstract void convert(ViewHolder holder,T t);
}
