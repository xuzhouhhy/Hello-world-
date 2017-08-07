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
import com.xuzhouhhy.baidumap.db.NavigatePointManage;
import com.xuzhouhhy.baidumap.util.UtilBaidu;

import java.util.ArrayList;
import java.util.Iterator;
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
        for (Block block : mBlocks) {
            Overlay pointMarker = getPointOverlay(block);
            Overlay titleMarker = getTitleOverlay(block);
            mBaiduBlockMarks.add(new BaiduBlockMark(block, pointMarker, titleMarker));
        }
    }

    private Overlay getTitleOverlay(Block block) {
        LatLng latLng = UtilBaidu.coorConverter84ToBaidu(block.getPoint());
        TextOptions textOptions = new TextOptions()
                .text("index : " + block.getMarkTitle())
                .align(TextOptions.ALIGN_CENTER_VERTICAL, TextOptions.ALIGN_TOP)
                .fontSize(30)
                .bgColor(android.support.v7.appcompat.R.color.abc_hint_foreground_material_dark)
                .fontColor(android.support.v7.appcompat.R.color.abc_btn_colored_borderless_text_material)
                .position(latLng)
                .rotate(0);
        return mBaiduMap.addOverlay(textOptions);
    }

    @NonNull
    private Overlay getPointOverlay(Block block) {
        //定义Maker坐标点
        LatLng latLng = UtilBaidu.coorConverter84ToBaidu(block.getPoint());
        //构建Marker图标
        BitmapDescriptor bitmap = UtilBaidu.getBitmapDescriptor();
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        Overlay marker = mBaiduMap.addOverlay(option);
        Bundle bundle = new Bundle();
        bundle.putString("mark_key", block.getMarkTitle());
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
    public boolean deleteSelect() {
        boolean ret = false;
        if (mBlockNumber == null || mBlockNumber.isEmpty()) {
            Toast.makeText(App.getInstance(), "请选择点", LENGTH_LONG);
            return false;
        }
        Iterator<BaiduBlockMark> iterator = mBaiduBlockMarks.iterator();
        while (iterator.hasNext()) {
            BaiduBlockMark blockMark = iterator.next();
            if (blockMark.getBlock().getMarkTitle().equalsIgnoreCase(mBlockNumber)) {
                blockMark.getPointMark().remove();
                blockMark.getTitleMark().remove();
                iterator.remove();
                if (blockMark.getBlock().isInput()) {
                    ret = NavigatePointManage.delete(blockMark.getBlock().getMarkTitle());
                } else {
                    ret = true;
                }
            }
        }
        return ret;
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

    /**
     * 添加地块点，包括数据和地图显示
     *
     * @param block 地块
     */
    public void addBlock(Block block) {
        Overlay pointMarker = getPointOverlay(block);
        Overlay titleMarker = getTitleOverlay(block);
        mBaiduBlockMarks.add(new BaiduBlockMark(block, pointMarker, titleMarker));
    }
}
