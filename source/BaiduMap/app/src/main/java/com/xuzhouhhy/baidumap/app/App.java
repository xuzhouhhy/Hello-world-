package com.xuzhouhhy.baidumap.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * application
 * Created by hhy on 2017/8/2.
 */

public class App extends Application {

    private static App instance = null;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SDKInitializer.initialize(this);
    }

}
