package com.ye.player.database.base;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseDao {
    protected static final String TAG = "BaseDao";

    protected SQLiteDatabase openDB(boolean write) {
        if (write) {
            return getDBHelper().getWritableDatabase();
        } else {
            return getDBHelper().getReadableDatabase();
        }
    }

    protected void closeDB(SQLiteDatabase db) {
        // Do not close, this is an empty method, if necessary we will implement it later
    }

    abstract protected SQLiteOpenHelper getDBHelper();
}
