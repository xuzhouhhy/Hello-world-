package com.xuzhouhhy.baidumap.db;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.xuzhouhhy.baidumap.app.App;
import com.xuzhouhhy.baidumap.data.Point3DMutable;

import static com.xuzhouhhy.baidumap.db.NavigationTable.LOCALE;
import static com.xuzhouhhy.baidumap.db.NavigationTable.LOCALH;
import static com.xuzhouhhy.baidumap.db.NavigationTable.LOCALN;
import static com.xuzhouhhy.baidumap.db.NavigationTable.POINT_NAME;
import static com.xuzhouhhy.baidumap.db.NavigationTable.POINT_TYPE;
import static com.xuzhouhhy.baidumap.db.NavigationTable.TABLE_NAME;
import static com.xuzhouhhy.baidumap.db.NavigationTable.WGSB;
import static com.xuzhouhhy.baidumap.db.NavigationTable.WGSH;
import static com.xuzhouhhy.baidumap.db.NavigationTable.WGSL;

/**
 * Created by user on 2017/8/6.
 */

public class NavigatePointManage {

    /**
     * 数据库本地导航点标记
     */
    public static final String LOCAL_POINT_MARK = "local";

    /**
     * 数据库WGS导航点标记
     */
    public static final String WGS_POINT_MARK = "wgs";

    /**
     * 添加导航点到数据库
     *
     * @param isLocal 本地点？
     * @param name    点名
     * @param point   坐标
     * @return 保存成功？
     */
    public static boolean addPoint(boolean isLocal, String name, Point3DMutable point) {
        if (isLocal) {
            return addLocalPoint(name, point);
        } else {
            return addWgsPoint(name, point);
        }
    }

    private static boolean addWgsPoint(String name, Point3DMutable point) {
        boolean ret = false;
        BdcDatabaseHelper bdcDb = App.getInstance().getBdcDbHelper();
        SQLiteDatabase db = bdcDb.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(POINT_NAME.getValue(), name);
            contentValues.put(POINT_TYPE.getValue(), WGS_POINT_MARK);
            contentValues.put(LOCALN.getValue(), 0.0);
            contentValues.put(LOCALE.getValue(), 0.0);
            contentValues.put(LOCALH.getValue(), 0.0);
            contentValues.put(WGSB.getValue(), point.getX());
            contentValues.put(WGSL.getValue(), point.getY());
            contentValues.put(WGSH.getValue(), point.getZ());
            ret = db.insert(TABLE_NAME.getValue(), null, contentValues) > -1;
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return ret;
    }

    private static boolean addLocalPoint(String name, Point3DMutable point) {
        boolean ret = false;
        BdcDatabaseHelper bdcDb = App.getInstance().getBdcDbHelper();
        SQLiteDatabase db = bdcDb.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(POINT_NAME.getValue(), name);
            contentValues.put(POINT_TYPE.getValue(), LOCAL_POINT_MARK);
            contentValues.put(LOCALN.getValue(), point.getX());
            contentValues.put(LOCALE.getValue(), point.getY());
            contentValues.put(LOCALH.getValue(), point.getZ());
            contentValues.put(WGSB.getValue(), 0.0);
            contentValues.put(WGSL.getValue(), 0.0);
            contentValues.put(WGSH.getValue(), 0.0);
            ret = db.insert(TABLE_NAME.getValue(), null, contentValues) > -1;
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return ret;
    }

}
