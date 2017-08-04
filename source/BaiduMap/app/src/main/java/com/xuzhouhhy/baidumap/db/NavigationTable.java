package com.xuzhouhhy.baidumap.db;

/**
 * Created by user on 2017/8/4.
 */

public enum NavigationTable {

    TABLE_NAME("navigate_point"),
    POINT_NAME("name"),
    POINT_TYPE("point_type"),
    LOCALN("localn"),
    LOCALE("locale"),
    LOCALH("localh"),
    WGSB("wgsb"),
    WGSL("wgsl"),
    WGSH("wgsh");

    private String mValue;

    NavigationTable(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
