package com.xuzhouhhy.baidumap;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.xuzhouhhy.baidumap.data.Point3DMutable;
import com.xuzhouhhy.baidumap.util.UtilBaidu;

import java.util.List;

/**
 * Created by user on 2017/8/3.
 */

class BaiduMapController {

    private BaiduMap mBaiduMap;

    private List<Point3DMutable> mPoints;

    private List<Overlay> mMarks;

    BaiduMapController(@NonNull BaiduMap baiduMap, @NonNull List<Point3DMutable> points) {
        mBaiduMap = baiduMap;
        mPoints = points;
        initOverlay();
    }

    private void initOverlay() {
        for (int i = 0; i<mPoints.size(); i++) {
            //定义Maker坐标点
            LatLng latLng = UtilBaidu.coorConverter84ToBaidu(mPoints.get(i));
            //构建Marker图标
            BitmapDescriptor bitmap = getBitmapDescriptor();
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(UtilBaidu.coorConverter84ToBaidu(latLng))
                    .icon(bitmap)
                    .title("s_12345_01");
            //在地图上添加Marker，并显示
            Overlay marker = mBaiduMap.addOverlay(option);
            marker.setZIndex(i);
            Bundle bundle = new Bundle();
            bundle.putString("mark_key", "31&121");
            marker.setExtraInfo(bundle);
        }
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

    public void setMarks(List<Overlay> marks) {
        mMarks = marks;
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

    private BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
    }

}
