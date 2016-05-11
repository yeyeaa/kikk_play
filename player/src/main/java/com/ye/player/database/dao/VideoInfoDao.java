package com.ye.player.database.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.ye.player.common.bean.VideoInfo;
import com.ye.player.database.base.UserSpecificBaseDao;

import java.util.ArrayList;
import java.util.List;

public class VideoInfoDao extends UserSpecificBaseDao {
    public static final String TABLE_VIDEOINFO = "shenmewanyi";

    public static final String ID = "a";
    public static final String PATH = "b";
    public static final String THUMB_PATH= "c";
    public static final String DANMA_PATH = "d";
    public static final String TITLE = "e";
    public static final String MIME_TYPE = "f";
    public static final String DURATION = "g";
    public static final String SIZE = "h";
    public static final String PAUSE = "i";

    private final static String TABLE_CREATE_VIDEOINFO = "CREATE TABLE IF NOT EXISTS " + TABLE_VIDEOINFO + " (" + ID
            + " VARCHAR, " + PATH + " VARCHAR, " + THUMB_PATH + " VARCHAR, " + DANMA_PATH + " VARCHAR, "
            + TITLE + " VARCHAR, " + MIME_TYPE + " VARCHAR, " + DURATION + " LONG, " + SIZE + " LONG, "
            + PAUSE + " LONG, "+ ");";


    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_VIDEOINFO);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertVideoList(List<VideoInfo> videoInfoList){
        SQLiteDatabase db = openDB(true);
        db.beginTransaction();

        try {
            String sql = "INSERT INTO " + TABLE_VIDEOINFO + " (" + ID + "," + PATH + "," + THUMB_PATH + ","
                    + DANMA_PATH + "," + TITLE + "," + MIME_TYPE + "," + DURATION + "," + SIZE
                    + "," + PAUSE + ") values (?,?,?,?,?,?,?,?,?)";

            SQLiteStatement stmt = db.compileStatement(sql);
            for (VideoInfo vi : videoInfoList) {
                stmt.bindString(1, vi.getId());
                if (vi.getPath() != null){
                    stmt.bindString(2, vi.getPath());
                }
                if (vi.getThumbPath() != null){
                    stmt.bindString(3, vi.getThumbPath());
                }
                if (vi.getDanmaPath() != null){
                    stmt.bindString(4, vi.getDanmaPath());
                }
                if (vi.getTitle() != null){
                    stmt.bindString(5, vi.getTitle());
                }
                if (vi.getMimeType() != null){
                    stmt.bindString(6, vi.getMimeType());
                }
                stmt.bindLong(7, vi.getDuration());
                stmt.bindLong(8, vi.getSize());
                stmt.bindLong(9, vi.getPause());
                stmt.execute();
                stmt.clearBindings();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            stmt.close();
        } catch (Exception e) {
            Log.e(TAG, "sql error", e);
        } finally {
            closeDB(db);
        }
    }

    public List<VideoInfo> getAllVideos() {
        SQLiteDatabase db = openDB(false);
        List<VideoInfo> list = new ArrayList<VideoInfo>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT distinct * FROM " + TABLE_VIDEOINFO + " ORDER BY "+ID, null);
            VideoInfo videoInfo = null;

            int videoIdIndex = 0;
            int videoPathIndex = 1;
            int videoThumbPathIndex = 2;
            int videoDanmaPathIndex = 3;
            int videoTitleIndex = 4;
            int videoMimeTypeIndex = 5;
            int videoDurationIndex = 6;
            int videoSizeIndex = 7;
            int videoPauseIndex = 8;
            while (cursor.moveToNext()) {
                videoInfo = new VideoInfo();
                videoInfo.setId(cursor.getString(videoIdIndex));
                videoInfo.setPath(cursor.getString(videoPathIndex));
                videoInfo.setThumbPath(cursor.getString(videoThumbPathIndex));
                videoInfo.setDanmaPath(cursor.getString(videoDanmaPathIndex));
                videoInfo.setTitle(cursor.getString(videoTitleIndex));
                videoInfo.setMimeType(cursor.getString(videoMimeTypeIndex));
                videoInfo.setDuration(cursor.getLong(videoDurationIndex));
                videoInfo.setSize(cursor.getLong(videoSizeIndex));
                videoInfo.setPause(cursor.getLong(videoPauseIndex));
                list.add(videoInfo);
            }
        } catch (SQLException e) {
            Log.e(TAG, "sql error", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDB(db);
        }
        return list;
    }

    public void deleteAllvideo() {
        SQLiteDatabase db = openDB(true);
        String sql = "delete from " + TABLE_VIDEOINFO;
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG, "sql error", e);
        } finally {
            closeDB(db);
        }
    }

    public void deleteVideo(String videoId) {
        SQLiteDatabase db = openDB(true);
        String sql = "delete from " + TABLE_VIDEOINFO /* TABLE_LS_AD */+ " where " + ID + "='" + videoId + "'";
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG, "sql error", e);
        } finally {
            closeDB(db);
        }
    }

}