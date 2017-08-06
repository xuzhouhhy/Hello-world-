package com.xuzhouhhy.baidumap.app;

import android.app.Application;
import android.database.SQLException;

import com.baidu.mapapi.SDKInitializer;
import com.xuzhouhhy.baidumap.Constant;
import com.xuzhouhhy.baidumap.db.BdcDatabaseHelper;

/**
 * application
 * Created by hhy on 2017/8/2.
 */

public class App extends Application {

    private static App instance = null;

    private BdcDatabaseHelper mBdcDbHelper;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SDKInitializer.initialize(this);
        try {
            mBdcDbHelper = new BdcDatabaseHelper(this,
                    Constant.getAppDataPath() + "Bdc/bdc.db");
            mBdcDbHelper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BdcDatabaseHelper getBdcDbHelper() {
        return mBdcDbHelper;
    }
}
