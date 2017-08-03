package com.xuzhouhhy.baidumap;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.xuzhouhhy.baidumap.data.Point3DMutable;
import com.xuzhouhhy.baidumap.util.UtilBaidu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/8/3.
 */

class BaiduMapController {

    private BaiduMap mBaiduMap;

    private List<Point3DMutable> mPoints;

    private List<Overlay> mMarks;

    private List<Overlay> mTitleMarks;

    BaiduMapController(@NonNull BaiduMap baiduMap, @NonNull List<Point3DMutable> points) {
        mBaiduMap = baiduMap;
        mPoints = points;
        mMarks = new ArrayList<>();
        mTitleMarks = new ArrayList<>();
        initPointMarker();
        initTitle();
    }

    private void initTitle() {
        for (int i = 0; i < mPoints.size(); i++) {
            LatLng latLng = UtilBaidu.coorConverter84ToBaidu(mPoints.get(i));
            TextOptions textOptions = new TextOptions()
                    .text("index : " + new Integer(i).toString())
                    .align(TextOptions.ALIGN_CENTER_VERTICAL, TextOptions.ALIGN_TOP)
                    .fontSize(20)
                    .bgColor(android.support.v7.appcompat.R.color.abc_hint_foreground_material_dark)
                    .fontColor(android.support.v7.appcompat.R.color.abc_btn_colored_borderless_text_material)
                    .position(latLng)
                    .zIndex(i)
                    .rotate(0);
            Overlay titleMarker = mBaiduMap.addOverlay(textOptions);
            mTitleMarks.add(titleMarker);
//            //创建InfoWindow展示的view
//            Button button = new Button(App.getInstance());
//            button.setBackgroundResource(R.drawable.pos_caution);
////            TextView tvTitle = new TextView(App.getInstance());
////            tvTitle.setText("index : " + new Integer(i).toString());
//            //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//            InfoWindow infoWindow = new InfoWindow(button, latLng, -47);
//            mWindows.add(infoWindow);
        }
    }

    private void initPointMarker() {
        for (int i = 0; i < mPoints.size(); i++) {
            Overlay marker = getOverlay(i);
            mMarks.add(marker);
        }
    }

    @NonNull
    private Overlay getOverlay(int i) {
        //定义Maker坐标点
        LatLng latLng = UtilBaidu.coorConverter84ToBaidu(mPoints.get(i));
        //构建Marker图标
        BitmapDescriptor bitmap = UtilBaidu.getBitmapDescriptor();
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap)
                .title("s_12345_01");
        //在地图上添加Marker，并显示
        Overlay marker = mBaiduMap.addOverlay(option);
        marker.setZIndex(i);
        Bundle bundle = new Bundle();
        bundle.putString("mark_key", "marker index : " + marker.getZIndex());
        marker.setExtraInfo(bundle);
        return marker;
    }

    public BaiduMap getBaiduMap() {
        return mBaiduMap;
    }

    public void setBaiduMap(BaiduMap baiduMap) {
        mBaiduMap = baiduMap;
    }

    public List<Overlay> getMarks() {
        return mMarks;
    }

    public List<Point3DMutable> getPoints() {
        return mPoints;
    }

    public void setPoints(List<Point3DMutable> points) {
        mPoints = points;
    }

    void setOnMarkerClickListener(BaiduMap.OnMarkerClickListener onMarkerClickListener) {
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
    }
}
