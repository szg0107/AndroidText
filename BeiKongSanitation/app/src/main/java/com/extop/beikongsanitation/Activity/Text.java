package com.extop.beikongsanitation.Activity;

import android.graphics.Color;
import android.os.Bundle;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

import com.amap.api.maps.model.PolylineOptions;
import com.extop.beikongsanitation.R;
import com.extop.beikongsanitation.Tool.NetWorkTools;
import com.extop.beikongsanitation.Tool.PublicActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/25. 测试在地图上画线和获取当前位置
 */

public class Text extends PublicActivity {
    private AMap aMap;
    private MapView mapView;//地图控件
    private MyLocationStyle myLocationStyle;//目前为止类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_line);
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//        MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        addPolylinessoild();//画实线




//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.showMyLocation(true);
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
//        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        /*按照传入的CameraUpdate参数改变地图状态。（直接改变状态，没有动画效果）
        可以通过CameraUpdateFactory.zoomIn()等方法来生成对应的CameraUpdate对象。*/
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.300299, 106.347656), 4));
//        设置地图底图文字标注的层级指数，默认为0，用来比较覆盖物（polyline、polygon、circle等）的zIndex。
//        aMap.setMapTextZIndex(2);
    }

    //绘制一条实线
    private void addPolylinessoild() {

        List<DPoint> examplePoint=new ArrayList<DPoint>();
        examplePoint.add(new DPoint(36.397755,120.430336));
        examplePoint.add(new DPoint(36.395256,120.430336));
        examplePoint.add(new DPoint(36.39293,120.43062));
        examplePoint.add(new DPoint(36.390255,120.43091));
        examplePoint.add(new DPoint(36.3867,120.43178));
        examplePoint.add(new DPoint(36.383507,120.43178));
        List<LatLng> latLngs = NetWorkTools.convert(examplePoint,getApplicationContext());
        aMap.addPolyline((new PolylineOptions())
                .addAll(latLngs)
                .width(10)
                .color(Color.argb(255, 1, 255, 255)));
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
