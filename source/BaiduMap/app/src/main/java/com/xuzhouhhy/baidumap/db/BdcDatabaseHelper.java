package com.xuzhouhhy.baidumap.db;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2017/8/4.
 */

public class BdcDatabaseHelper extends SQLiteOpenHelper {

    public static final String BDC_DB_NAME = "bdc.db";

    public static final String CREATE_POINT_TABLE = "create table if ont exists navigate_point(name text primary key)";

}
