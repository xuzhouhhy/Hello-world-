package com.xuzhouhhy.baidumap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.xuzhouhhy.baidumap.app.App;
import com.xuzhouhhy.baidumap.data.BaiduBlockMark;
import com.xuzhouhhy.baidumap.data.Block;
import com.xuzhouhhy.baidumap.util.UtilBaidu;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by user on 2017/8/3.
 */

class BaiduMapController {

    private BaiduMap mBaiduMap;

    private String mBlockNumber = "";

    private List<BaiduBlockMark> mBaiduBlockMarks;

    private List<Block> mBlocks;

    BaiduMapController(@NonNull BaiduMap baiduMap, @NonNull List<Block> blocks) {
        mBaiduMap = baiduMap;
        mBaiduBlockMarks = new ArrayList<>();
        mBlocks = blocks;
        initPointMarker();
    }

    private void initPointMarker() {
        for (int i = 0; i < mBlocks.size(); i++) {
            Overlay pointMarker = getPointOverlay(i);
            Overlay titleMarker = getTitleOverlay(i);
            mBaiduBlockMarks.add(new BaiduBlockMark(mBlocks.get(i), pointMarker, titleMarker));
        }
    }

    private Overlay getTitleOverlay(int i) {
        LatLng latLng = UtilBaidu.coorConverter84ToBaidu(mBlocks.get(i).getPoint());
        TextOptions textOptions = new TextOptions()
                .text("index : " + new Integer(i).toString())
                .align(TextOptions.ALIGN_CENTER_VERTICAL, TextOptions.ALIGN_TOP)
                .fontSize(30)
                .bgColor(android.support.v7.appcompat.R.color.abc_hint_foreground_material_dark)
                .fontColor(android.support.v7.appcompat.R.color.abc_btn_colored_borderless_text_material)
                .position(latLng)
                .zIndex(i)
                .rotate(0);
        return mBaiduMap.addOverlay(textOptions);
    }

    @NonNull
    private Overlay getPointOverlay(int i) {
        //定义Maker坐标点
        LatLng latLng = UtilBaidu.coorConverter84ToBaidu(mBlocks.get(i).getPoint());
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
        bundle.putString("mark_key", mBlocks.get(i).getMarkTitle());
        marker.setExtraInfo(bundle);
        return marker;
    }

    public BaiduMap getBaiduMap() {
        return mBaiduMap;
    }

    public List<BaiduBlockMark> getBaiduBlockMarks() {
        return mBaiduBlockMarks;
    }

    void setOnMarkerClickListener(BaiduMap.OnMarkerClickListener onMarkerClickListener) {
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
    }

    public void setBlockNumber(String blockNumber) {
        mBlockNumber = blockNumber;
    }

    /**
     * delete select block mark
     */
    public void deleteSelect() {
        if (mBlockNumber == null || mBlockNumber.isEmpty()) {
            Toast.makeText(App.getInstance(), "请选择点", LENGTH_LONG);
            return;
        }
        for (BaiduBlockMark blockMark : mBaiduBlockMarks) {
            if (blockMark.getBlock().getMarkTitle().equalsIgnoreCase(mBlockNumber)) {
                blockMark.getPointMark().remove();
                blockMark.getTitleMark().remove();
                mBaiduBlockMarks.remove(blockMark);
                return;
            }
        }
    }

    public Intent getNavigationIntent() {
        if (mBlockNumber == null || mBlockNumber.isEmpty()) {
            Toast.makeText(App.getInstance(), "请选择点", LENGTH_LONG);
            return null;
        }
        Overlay pointMark;
        for (BaiduBlockMark blockMark : mBaiduBlockMarks) {
            if (blockMark.getBlock().getMarkTitle().equalsIgnoreCase(mBlockNumber)) {
                pointMark = blockMark.getPointMark();
                if (pointMark == null) {
                    Toast.makeText(App.getInstance(), "目标点无坐标", LENGTH_LONG);
                    return null;
                }
                return getIntent((Marker) pointMark);
            }
        }
        return null;
    }

    @NonNull
    private Intent getIntent(Marker pointMark) {
        LatLng latLng = pointMark.getPosition();
        String lat = String.valueOf(latLng.latitude);
        String lon = String.valueOf(latLng.longitude);
        String uriDes = lat + "," + lon;
        Intent intent = new Intent();
        // 驾车路线规划
        intent.setData(Uri.parse("baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=" + uriDes + "&mode=driving"));
        return intent;
    }
}
