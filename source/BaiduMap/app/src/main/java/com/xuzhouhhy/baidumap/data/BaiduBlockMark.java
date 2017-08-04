package com.xuzhouhhy.baidumap.data;

import com.baidu.mapapi.map.Overlay;

/**
 * 百度地图地块标记，包括点坐标、点标记、文字标记
 * Created by hhy on 2017/8/4.
 */

public class BaiduBlockMark {

    private Block mBlock;

    /**
     * 点标记
     */
    private Overlay mPointMark;

    /**
     * 文字标记
     */
    private Overlay mTitleMark;

    public BaiduBlockMark(Block block, Overlay pointMark, Overlay titleMark) {
        mBlock = block;
        mPointMark = pointMark;
        mTitleMark = titleMark;
    }

    public Block getBlock() {
        return mBlock;
    }

    public Overlay getPointMark() {
        return mPointMark;
    }

    public Overlay getTitleMark() {
        return mTitleMark;
    }

}
