package com.extop.beikongsanitation.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.extop.beikongsanitation.Bean.RecordBean;
import com.extop.beikongsanitation.MyApplication;
import com.extop.beikongsanitation.R;
import com.extop.beikongsanitation.Tool.CommonAdapter;
import com.extop.beikongsanitation.Tool.NetWorkTools;
import com.extop.beikongsanitation.Tool.PublicActivity;
import com.extop.beikongsanitation.Tool.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 * 垃圾收运详情
 */

public class GarbageCollectorDetailsActivity extends PublicActivity implements GeocodeSearch.OnGeocodeSearchListener, OnLoadMoreListener {
    // 菜单列表
    private ListView lv_personal;
    // 存储菜单名称
    private ArrayList<RecordBean> arrayList = new ArrayList<RecordBean>();
    private CommonAdapter<RecordBean> adapter;
    private Toolbar toolbar;
    // 坐标数组
    private List<LatLonPoint> getPointList = new ArrayList<LatLonPoint>();
    // 逆地理编码需要的变量
    private GeocodeSearch geocodeSearch;
    // 返回数据数组
    private JSONArray dataDetail = new JSONArray();
    // 单个数据
    private JSONObject dateValue = new JSONObject();
    // 地址数组
    private List<String> addressArray = new ArrayList<String>();
    //记录逆地理编码是否完全转换完
    private int returnIndex = 0;
    private Intent intent;
    //上拉加载更多布局
    SwipeToLoadLayout swipeToLoadLayout;
    // 页数
    private int pageNumber = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);
        //获得刷新布局控件
        swipeToLoadLayout = findViewById(R.id.swipeToLoadLayout);
        //设置加载更多监听
        swipeToLoadLayout.setOnLoadMoreListener(this);
        //listView控件
        lv_personal = findViewById(R.id.swipe_target);
        //获取Toolbar控件
        toolbar = findViewById(R.id.toolbar);
        //设置返回按钮并添加监听事件
        toolbar.setNavigationIcon(R.mipmap.arrow_left_d);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        GetRecordDetail(4 + "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (dataDetail.length() == arrayList.size()) {
            //设置上拉加载不可用
            swipeToLoadLayout.setLoadMoreEnabled(false);
        }
    }

    //操作记录
    private void GetRecordDetail(String operateTypeId) {
        //MyApplication.identificationCode, MyApplication.carId, MyApplication.driverId
        //868861021207911 7 3
        NetWorkTools.GetRecordDetail(MyApplication.identificationCode, MyApplication.carId, MyApplication.driverId, operateTypeId, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("返回的值", NetWorkTools.replaceBlank(result));
                try {
                    //获取全部返回值
                    JSONObject json = new JSONObject(NetWorkTools.replaceBlank(result));
                    JSONArray dataArray = json.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        dateValue.put("code", dataArray.getJSONObject(i).get("code"));
                        dateValue.put("fullscale", dataArray.getJSONObject(i).get("fullscale"));
                        dateValue.put("time", dataArray.getJSONObject(i).get("time"));
                        dateValue.put("address", "");
                        dataDetail.put(i, dateValue);
                        getPointList.add(new LatLonPoint(Double.parseDouble(dataArray.getJSONObject(i).getString("lat")), Double.parseDouble(dataArray.getJSONObject(i).getString("lng"))));
                    }
                    getAddress();
                    Log.d("记录数据的长度", dataDetail.length() + "");
                    Log.d("坐标数组长度", getPointList.size() + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("错误", ex + "");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 响应逆地理编码
     */
    public void getAddress() {
        for (final LatLonPoint point : getPointList) {
            RegeocodeQuery query = new RegeocodeQuery(point, 200,
                    GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            geocodeSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
            returnIndex++;
        }
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {

                Log.d("-!--***********", result.getRegeocodeAddress().getFormatAddress() + "NOW");
                addressArray.add(result.getRegeocodeAddress().getFormatAddress() + "附近");
            } else {
            }
        } else {
            Toast.makeText(GarbageCollectorDetailsActivity.this, rCode + "", Toast.LENGTH_LONG).show();
        }

        returnIndex--;
        if (returnIndex == 0) {
            Log.d("-!--*****结束******", addressArray.size() + "");
            for (int i = 0; i < dataDetail.length(); i++) {
                try {
                    dataDetail.getJSONObject(i).put("address", addressArray.get(i));
                    if (i < 10) {
                        arrayList.add(new RecordBean("垃圾箱编号", dataDetail.getJSONObject(i).getString("code"), "满度            ", dataDetail.getJSONObject(i).getString("fullscale"), "时间            ", dataDetail.getJSONObject(i).getString("time"),"地址            ", dataDetail.getJSONObject(i).getString("address")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter = new CommonAdapter<RecordBean>(getApplicationContext(), arrayList, R.layout.record_contents) {
                @Override
                public void convert(ViewHolder holder, RecordBean recordBean) {
                    holder.setText(R.id.startTime, recordBean.getStartTime());
                    holder.setText(R.id.startTimeValue, recordBean.getStartTimeValue());
                    holder.setText(R.id.startPosition, recordBean.getStartPosition());
                    holder.setText(R.id.startPositionValue, recordBean.getStartPositionValue());
                    holder.setText(R.id.endTime, recordBean.getEndTime());
                    holder.setText(R.id.endTimeValue, recordBean.getEndTimeValue());
                    holder.setText(R.id.endPosition, recordBean.getEndPosition());
                    holder.setText(R.id.endPositionValue, recordBean.getEndPositionValue());
                }

            };
            lv_personal.setAdapter(adapter);
            Log.d("-!--*****结束******", dataDetail.toString() + "");
        }

    }


    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
    }

    //上拉加载更多
    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
                //如果总数据剩余数据不够10条
                if (dataDetail.length() - (pageNumber * 10) > 0 && dataDetail.length() - (pageNumber * 10) < 10) {
                    for (int i = 10 * pageNumber; i < dataDetail.length(); i++) {
                        try {
                            arrayList.add(new RecordBean("垃圾箱编号", dataDetail.getJSONObject(i).getString("code"), "满度            ", dataDetail.getJSONObject(i).getString("fullscale"), "时间            ", dataDetail.getJSONObject(i).getString("time"),"地址            ", dataDetail.getJSONObject(i).getString("address")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (int i = 10 * pageNumber; i <= (10 * pageNumber + 9); i++) {
                        try {
                            arrayList.add(new RecordBean("垃圾箱编号", dataDetail.getJSONObject(i).getString("code"), "满度            ", dataDetail.getJSONObject(i).getString("fullscale"), "时间            ", dataDetail.getJSONObject(i).getString("time"),"地址            ", dataDetail.getJSONObject(i).getString("address")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //适配器刷新
                adapter.notifyDataSetChanged();
                pageNumber++;
            }
        }, 2000);
    }
}
