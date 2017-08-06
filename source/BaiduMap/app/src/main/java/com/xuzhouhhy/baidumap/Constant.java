package com.xuzhouhhy.baidumap;

import com.xuzhouhhy.baidumap.util.UtilSDCardManager;

/**
 * Created by user on 2017/8/6.
 */

public class Constant {

    public static String getAppDataPath() {
        return UtilSDCardManager.GetFirstSDCardPath() + "CHCNAV/LandStar7/";
    }

}
