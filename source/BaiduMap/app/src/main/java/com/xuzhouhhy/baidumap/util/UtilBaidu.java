package com.xuzhouhhy.baidumap.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * Created by user on 2017/8/3.
 */

public class UtilBaidu {

    public static LatLng coorConverter84ToBaidu(LatLng src84Blh) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(src84Blh);
        return converter.convert();
    }

}
