package com.example.administrator.firstlineofcodetest.ActivityTest;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.firstlineofcodetest.R;

import java.util.List;

/**
 * Created by Administrator on 2018/3/11.
 * 滚动布局适配器
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Fruit> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;
        ImageView item_list_image;
        TextView item_list_title;
        public ViewHolder(View itemView) {
            super(itemView);
            //保存子项最外层布局的实例
            fruitView=itemView;
            //获取图片控件
            item_list_image = (ImageView) itemView.findViewById(R.id.item_list_image);
            //获取文字控件
            item_list_title = (TextView) itemView.findViewById(R.id.item_list_title);
        }
    }

    public RecyclerViewAdapter(List<Fruit> fruitList) {
        mFruitList = fruitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //用于创建ViewHolder实例
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //设置点击事件
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户点击的position
                int position = holder.getAdapterPosition();
                //通过position获取实例
                Fruit fruit=mFruitList.get(position);
                Toast.makeText(v.getContext(),"you clicked view"+fruit.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.item_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =holder.getAdapterPosition();
                Fruit fruit=mFruitList.get(position);
                Toast.makeText(v.getContext(),"you clicked image" +fruit.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //对RecycleView子项的数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
        Fruit fruit=mFruitList.get(position);
        holder.item_list_image.setImageResource(fruit.getImageId());
        holder.item_list_title.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        //告诉RecyclerView一共有多少子项，直接返回数据源的长度
        return mFruitList.size();
    }

}
