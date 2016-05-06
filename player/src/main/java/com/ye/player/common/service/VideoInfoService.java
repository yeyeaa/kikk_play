package com.ye.player.common.service;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.ye.player.common.bean.VideoInfo;
import com.ye.player.common.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoInfoService {

    private Context context;

    public VideoInfoService(Context context){
        this.context = context;
    }

    public void updateVideoInfo(Handler handler){
        //更新数据库
        handler.sendMessage(new Message());
    }

    public List getVideoInfo(){
        //从数据库取出

        String[] mediaColumns = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
        };

        //首先检索SDcard上所有的video
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
        List<VideoInfo> videoList = new ArrayList<VideoInfo>();

        if(cursor.moveToFirst()){
            do{
                VideoInfo info = new VideoInfo();

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                info.setId(String.valueOf(id));
                info.setPath(path);
                info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
                info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
                info.setDuration(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                info.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));

                info.setThumbPath(getThumbPath(id));
                info.setDanmaPath(getDanmaPath(path));

                videoList.add(info);
            }while(cursor.moveToNext());
        }
        return videoList;

    }

    private String getThumbPath(int id) {
        //根据该ID获取其Thumb
        String[] thumbColumns = new String[]{
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID
        };


        String selection = MediaStore.Video.Thumbnails.VIDEO_ID +"=?";
        String[] selectionArgs = new String[]{
                id+""
        };
        Cursor thumbCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs, null);
       // Cursor thumbCursor = ((Activity)context).managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs, null);

        if(thumbCursor.moveToFirst()){
            return thumbCursor.getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
        }
        return null;
    }

    private String getDanmaPath(String videoPath) {
        String danmaPath = videoPath.substring(0,videoPath.lastIndexOf("."))+".xml";
        if (FileUtils.isFileExists(danmaPath)) {
            return "file://" + danmaPath;
        }else {
            return null;
        }
    }
}
