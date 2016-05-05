package com.ye.player.datebase.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ye.player.datebase.dao.VideoInfoDao;

public class UserSpecificDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 10;

    public UserSpecificDBHelper(Context context, String userId) {
        super(context, userId + "-raw.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        VideoInfoDao.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        VideoInfoDao.onUpgrade(db, oldVersion, newVersion);
    }
}
