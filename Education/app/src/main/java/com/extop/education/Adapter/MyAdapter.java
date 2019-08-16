package com.extop.education.Adapter;

import android.content.Context;
import android.widget.ImageView;


import com.extop.education.MyApplication;
import com.extop.education.R;
import com.extop.education.model.CircleBean;
import com.extop.education.model.CircleParentBean;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class MyAdapter<T> extends CommonAdapter{
//实现自定义万能适配器
    public MyAdapter(Context context, List datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, Object o) {
        holder.setText(R.id.set_content,mDatas.get(mPosition).toString());
//        ImageView icon= (ImageView) holder.getView(R.id.circle_icon);
//        holder.setText(R.id.circle_name, MyApplication.circleParentBean.getData().get(mPosition).getCircleName());
//        holder.setImageURL(R.id.circle_icon,MyApplication.circleParentBean.getData().get(mPosition).getIcon()[mPosition]);
    }
}
