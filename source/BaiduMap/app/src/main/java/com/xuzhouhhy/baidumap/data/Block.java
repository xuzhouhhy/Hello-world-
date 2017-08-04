package com.xuzhouhhy.baidumap.data;

/**
 * Created by user on 2017/8/4.
 */

public class Block {

    /**
     * 地块中心点，有shape文件读取
     */
    private Point3DMutable mPoint;

    /**
     * 地块编号
     */
    private String mBlockNumber;

    public Block(Point3DMutable point, String blockNumber) {
        mPoint = point;
        mBlockNumber = blockNumber;
    }

    public Point3DMutable getPoint() {
        return mPoint;
    }

    public String getBlockNumber() {
        return mBlockNumber;
    }
}
