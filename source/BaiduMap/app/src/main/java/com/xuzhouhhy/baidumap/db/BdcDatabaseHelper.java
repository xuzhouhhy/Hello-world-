package com.xuzhouhhy.baidumap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
 * Created by user on 2017/8/4.
 */

public class BdcDatabaseHelper extends SQLiteOpenHelper {

    public static final String BDC_DB_NAME = "bdc.db";

    public static final String CREATE_POINT_TABLE = "create table if not exists "
            + TABLE_NAME.getValue()
            + " ( " + POINT_NAME.getValue() + " text , "
            + POINT_TYPE.getValue() + " text, "
            + LOCALN.getValue() + " real, "
            + LOCALE.getValue() + " real, "
            + LOCALH.getValue() + " real, "
            + WGSB.getValue() + " real, "
            + WGSL.getValue() + " real, "
            + WGSH.getValue() + " real )";

    public BdcDatabaseHelper(Context context, String dbPath) {
        super(context, dbPath, null, DbVersion.current().getValue());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_POINT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
