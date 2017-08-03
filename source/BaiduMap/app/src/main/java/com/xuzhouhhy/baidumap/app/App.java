package com.xuzhouhhy.baidumap.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * application
 * Created by hhy on 2017/8/2.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }

}
