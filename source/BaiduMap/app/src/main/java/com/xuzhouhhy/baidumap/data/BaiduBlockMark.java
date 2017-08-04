package com.xuzhouhhy.baidumap.data;

import com.baidu.mapapi.map.Overlay;

/**
 * 百度地图地块标记，包括点坐标、点标记、文字标记
 * Created by hhy on 2017/8/4.
 */

public class BaiduBlockMark {

    private Point3DMutable mPoint;

    /**
     * 点标记
     */
    private Overlay mPointMark;

    /**
     * 文字标记
     */
    private Overlay mTitleMark;

    public BaiduBlockMark(Point3DMutable point, Overlay pointMark, Overlay titleMark) {
        mPoint = point;
        mPointMark = pointMark;
        mTitleMark = titleMark;
    }

    public Point3DMutable getPoint() {
        return mPoint;
    }

    public void setPoint(Point3DMutable point) {
        mPoint = point;
    }

    public Overlay getPointMark() {
        return mPointMark;
    }

    public void setPointMark(Overlay pointMark) {
        mPointMark = pointMark;
    }

    public Overlay getTitleMark() {
        return mTitleMark;
    }

    public void setTitleMark(Overlay titleMark) {
        mTitleMark = titleMark;
    }
}
