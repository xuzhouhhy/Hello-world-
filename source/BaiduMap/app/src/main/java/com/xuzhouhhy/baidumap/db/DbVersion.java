package com.xuzhouhhy.baidumap.db;

/**
 * Created by hanhongyun on 2017/8/4.
 */

public enum DbVersion {

    VERSION_721(721);

    private int mValue;

    public int getValue() {
        return mValue;
    }

    DbVersion(int value) {
        mValue = value;
    }

    /**
     * Returns 当前版本号
     */
    public static DbVersion current() {
        return VERSION_721;
    }

}
