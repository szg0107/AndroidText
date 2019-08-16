package com.example.administrator.firstlineofcodetest.ActivityTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.example.administrator.firstlineofcodetest.R;
import java.util.ArrayList;
import java.util.List;


public class ActivityLifecycle extends AppCompatActivity {
    private static final String TAG = "ActivityLifecycle";
    List <Fruit> mData=new ArrayList<Fruit>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);
        //知晓当前是在哪一个活动
        Log.d(TAG, "onCreate: "+getClass().getSimpleName());
        //处理activity回收之前保存的值
        if (savedInstanceState!=null){
            Log.d(TAG, "activity回收之前保存的值"+savedInstanceState.getString("data_key"));
        }
        initFruits();//初始化水果数据
        //滚动布局
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //将recyclerView布局排列方式改为水平
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        //滚动布局适配器
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(mData);
        recyclerView.setAdapter(adapter);
    }

    private void initFruits(){
        for (int i=0;i<2;i++){
            Fruit apple=new Fruit("苹果",R.drawable.test);
            mData.add(apple);
            Fruit banana=new Fruit("香蕉",R.drawable.test2);
            mData.add(banana);
            Fruit orange=new Fruit("橙子",R.drawable.test3);
            mData.add(orange);
            Fruit watermelon=new Fruit("西瓜",R.drawable.test4);
            mData.add(watermelon);
            Fruit pear=new Fruit("梨",R.drawable.test);
            mData.add(pear);
            Fruit grape=new Fruit("葡萄",R.drawable.test2);
            mData.add(grape);
            Fruit pineapple=new Fruit("菠萝",R.drawable.test3);
            mData.add(pineapple);
            Fruit strawberry=new Fruit("草莓",R.drawable.test3);
            mData.add(strawberry);
            Fruit cherry=new Fruit("樱桃",R.drawable.test3);
            mData.add(cherry);
            Fruit mango=new Fruit("芒果",R.drawable.test3);
            mData.add(mango);
        }
    }
    /**
     * 在活动被回收之前一定会调用，用于解决活动被回收时临时数据得不到保存的问题
     * onSaveInstanceState携带一个Bundle类型的参数，Bundle提供了一系列的方法用于保存数据。
     * 每个保存方法需要传入两个参数数，
     * 第一个参数是键，用于后面从Bundle中取值,
     * 第二个参数是真正要保存的内容。
     **/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String tempData = "要保存的数据";
        outState.putString("data_key",tempData);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }
}
