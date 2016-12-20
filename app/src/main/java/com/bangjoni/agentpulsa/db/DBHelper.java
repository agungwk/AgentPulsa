package com.bangjoni.agentpulsa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by agung.kurniawan on 12/7/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "agents";

    private static final String TABLE_USER = "registered_user";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_IS_LOGGED_IN = "is_logged_in";

    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + COL_ID + " INTEGER PRIMARY KEY NOT NULL ," + COL_USERNAME
            + " TEXT," + COL_IS_LOGGED_IN + " INTEGER"+ ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL(CREATE_TABLE_USER);
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

    public String getLoggedInID() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_USER+" WHERE "+COL_IS_LOGGED_IN+" = 1";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            return c.getString(c.getColumnIndex(COL_USERNAME));
        }
        return null;
    }

    public void updateLogin(String username) {
        SQLiteDatabase readDb = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_USER+" WHERE "+COL_USERNAME+" = '"+username+"'";

        Cursor c = readDb.rawQuery(query, null);
        if (c.moveToFirst()) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_IS_LOGGED_IN, 1);

            db.update(TABLE_USER, values, COL_USERNAME+" = ?", new String[] { username });
        } else {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_USERNAME, username);
            values.put(COL_IS_LOGGED_IN, 1);

            db.insert(TABLE_USER, null, values);
        }
    }

    public void updateLogout(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
    }


}
