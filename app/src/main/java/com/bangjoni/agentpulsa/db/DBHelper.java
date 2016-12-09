package com.bangjoni.agentpulsa.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by agung.kurniawan on 12/7/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "agents";

    private static final String TABLE_VOUCHER = "order";
    private static final String COL_ID = "id";
    private static final String COL_CREATED_TIME = "created_time";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_TITLE = "title";
    private static final String COL_IMG_URL = "img_url";

    private static final String TABLE_USER = "registered_user";
    private static final String COL_USERNAME = "username";
    private static final String COL_IS_LOGGED_IN = "is_logged_in";

    private static final String TABLE_DEVICE_TOKEN = "device_token";
    private static final String COL_DEVICE_TOKEN = "token";

    private static final String CREATE_TABLE_VOUCHER = "CREATE TABLE "
            + TABLE_VOUCHER + "(" + COL_ID + " INTEGER PRIMARY KEY NOT NULL ," + COL_TITLE
            + " TEXT," + COL_DESCRIPTION + " TEXT," + COL_IMG_URL + " TEXT," + COL_CREATED_TIME
            + " DATETIME" + ")";
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + COL_ID + " INTEGER PRIMARY KEY NOT NULL ," + COL_USERNAME
            + " TEXT," + COL_IS_LOGGED_IN + " INTEGER"+ ")";
    private static final String CREATE_TABLE_DEVICE_TOKEN = "CREATE TABLE "
            + TABLE_DEVICE_TOKEN + "(" + COL_DEVICE_TOKEN + " TEXT" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOUCHER);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE_TOKEN);
//        db.execSQL(CREATE_TABLE_VOUCHER);
//        db.execSQL(CREATE_TABLE_USER);
//        db.execSQL(CREATE_TABLE_DEVICE_TOKEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

}
