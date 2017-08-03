package com.xuzhouhhy.baidumap.util;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.xuzhouhhy.baidumap.R;
import com.xuzhouhhy.baidumap.data.Point3DMutable;

/**
 * Created by user on 2017/8/3.
 */

public class UtilBaidu {

    public static LatLng coorConverter84ToBaidu(LatLng src84Blh) {
        return new CoordinateConverter()
                .from(CoordinateConverter.CoordType.GPS)
                .coord(src84Blh)
                .convert();
    }

    public static LatLng coorConverter84ToBaidu(Point3DMutable src84Blh) {
        LatLng latLng = new LatLng(src84Blh.getX(), src84Blh.getY());
        return coorConverter84ToBaidu(latLng);
    }

    /**
     * 被点击的点图标
     *
     * @return 被点击的点图标
     */
    public static BitmapDescriptor getClickBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.plus);
    }


    /**
     * 正常点图标
     *
     * @return 正常点图标
     */
    public static BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
    }

}
