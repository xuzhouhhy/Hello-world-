package com.xuzhouhhy.baidumap.data;

/**
 * Created by user on 2017/8/4.
 */

public class Block {

    /**
     * 地块中心点，shape文件读取 or 输入导航点
     */
    private Point3DMutable mPoint;

    /**
     * 地块编号 or 导航点名
     */
    private String mMarkTitle;

    public Block(Point3DMutable point, String markTitle) {
        mPoint = point;
        mMarkTitle = markTitle;
    }

    public Point3DMutable getPoint() {
        return mPoint;
    }

    public String getMarkTitle() {
        return mMarkTitle;
    }
}
