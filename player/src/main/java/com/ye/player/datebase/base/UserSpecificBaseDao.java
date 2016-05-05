package com.ye.player.datebase.base;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.ye.player.KikkApplication;
import com.ye.player.datebase.helper.UserSpecificDBHelper;

import java.util.HashMap;
import java.util.Map;

public class UserSpecificBaseDao extends BaseDao {
    private static final String userId = "buhuishujuku";
    private static Map<String, SQLiteOpenHelper> dbMap = new HashMap<String, SQLiteOpenHelper>();

    @Override
    protected SQLiteOpenHelper getDBHelper() {
        SQLiteOpenHelper dbHelper = dbMap.get(userId);
        if (dbHelper == null) {
            synchronized (dbMap) {
                dbHelper = dbMap.get(userId);
                if (dbHelper == null) {
                    dbHelper = new UserSpecificDBHelper(KikkApplication.app.getApplicationContext(), userId);
                    dbMap.put(userId, dbHelper);
                }
            }
        }

        return dbHelper;
    }
}