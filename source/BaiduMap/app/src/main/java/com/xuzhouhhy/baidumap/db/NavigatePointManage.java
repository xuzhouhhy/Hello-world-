package com.xuzhouhhy.baidumap.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.xuzhouhhy.baidumap.app.App;
import com.xuzhouhhy.baidumap.data.Block;
import com.xuzhouhhy.baidumap.data.Point3DMutable;

import java.util.ArrayList;
import java.util.List;

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
    private static final String LOCAL_POINT_MARK = "local";

    /**
     * 数据库WGS导航点标记
     */
    private static final String WGS_POINT_MARK = "wgs";

    /**
     * 添加导航点到数据库
     *
     * @param isLocal 本地点？
     * @param name    点名
     * @param point   坐标
     * @return 保存成功？
     */
    public static boolean addPoint(boolean isLocal, String name, Point3DMutable point) {
        BdcDatabaseHelper bdcDb = App.getInstance().getBdcDbHelper();
        SQLiteDatabase db = bdcDb.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME.getValue(), new String[]{POINT_NAME.getValue()},
                "name = ?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            boolean ret = updateVallue(isLocal, name, point);
            cursor.close();
            db.close();
            bdcDb.close();
            return ret;
        }
        if (isLocal) {
            return addLocalPoint(name, point);
        } else {
            return addWgsPoint(name, point);
        }
    }

    private static boolean updateVallue(boolean isLocal, String name, Point3DMutable point) {
        if (isLocal) {
            return updateLocalPoint(name, point);
        } else {
            return updateWgsPoint(name, point);
        }
    }

    private static boolean updateWgsPoint(String name, Point3DMutable point) {
        boolean ret = false;
        BdcDatabaseHelper bdcDb = App.getInstance().getBdcDbHelper();
        SQLiteDatabase db = bdcDb.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(POINT_TYPE.getValue(), WGS_POINT_MARK);
            contentValues.put(LOCALN.getValue(), 0.0);
            contentValues.put(LOCALE.getValue(), 0.0);
            contentValues.put(LOCALH.getValue(), 0.0);
            contentValues.put(WGSB.getValue(), point.getX());
            contentValues.put(WGSL.getValue(), point.getY());
            contentValues.put(WGSH.getValue(), point.getZ());
            ret = db.update(TABLE_NAME.getValue(), contentValues, "name = ?", new String[]{name}) > 0;
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

    private static boolean updateLocalPoint(String name, Point3DMutable point) {
        boolean ret = false;
        BdcDatabaseHelper bdcDb = App.getInstance().getBdcDbHelper();
        SQLiteDatabase db = bdcDb.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(POINT_TYPE.getValue(), LOCAL_POINT_MARK);
            contentValues.put(LOCALN.getValue(), point.getX());
            contentValues.put(LOCALE.getValue(), point.getY());
            contentValues.put(LOCALH.getValue(), point.getZ());
            contentValues.put(WGSB.getValue(), 0.0);
            contentValues.put(WGSL.getValue(), 0.0);
            contentValues.put(WGSH.getValue(), 0.0);
            ret = db.update(TABLE_NAME.getValue(), contentValues, "name = ?", new String[]{name}) > 0;
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

    /**
     * 删除导航点
     *
     * @param name 点名
     * @return 删除成功？
     */
    public static boolean delete(String name) {
        BdcDatabaseHelper bdcDb = App.getInstance().getBdcDbHelper();
        SQLiteDatabase db = bdcDb.getWritableDatabase();
        return db.delete(TABLE_NAME.getValue(), "name = ?", new String[]{name}) > 0;
    }

    public static List<Block> queryAll() {
        BdcDatabaseHelper bdcDb = App.getInstance().getBdcDbHelper();
        SQLiteDatabase db = bdcDb.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME.getValue(), new String[]{POINT_NAME.getValue(),
                LOCALN.getValue(),
                LOCALE.getValue(),
                LOCALH.getValue(),
                WGSB.getValue(),
                WGSL.getValue(),
                WGSH.getValue()}, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int c = cursor.getColumnCount();
                String name = cursor.getString(cursor.getColumnIndex(POINT_NAME.getValue()));
                String type = cursor.getString(cursor.getColumnIndex(POINT_TYPE.getValue()));
                double localN = cursor.getDouble(cursor.getColumnIndex(LOCALN.getValue()));
                double localE = cursor.getDouble(cursor.getColumnIndex(LOCALE.getValue()));
                double localH = cursor.getDouble(cursor.getColumnIndex(LOCALH.getValue()));
                double wgsB = cursor.getDouble(cursor.getColumnIndex(WGSB.getValue()));
                double wgsL = cursor.getDouble(cursor.getColumnIndex(WGSL.getValue()));
                double wgsH = cursor.getDouble(cursor.getColumnIndex(WGSH.getValue()));
                if (type != null) {
                    if (type.trim().equalsIgnoreCase(LOCAL_POINT_MARK)) {
                        Point3DMutable point = new Point3DMutable(localN, localE, localH);
                        Block block = new Block(point, name, true);
                    } else if (type.trim().equalsIgnoreCase(WGS_POINT_MARK)) {
                        Point3DMutable point = new Point3DMutable(wgsB, wgsL, wgsH);
                        Block block = new Block(point, name, true);
                    }
                }
            }
            cursor.close();
        }
        return new ArrayList<>();
    }
}
