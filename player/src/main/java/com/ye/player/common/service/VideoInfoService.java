package com.ye.player.common.service;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.ye.player.common.bean.VideoInfo;

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


        String[] thumbColumns = new String[]{
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID
        };

        String[] mediaColumns = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE
        };

        //首先检索SDcard上所有的video
        //过时方法要换掉
        Cursor cursor = ((Activity)context).managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);

        List<VideoInfo> videoList = new ArrayList<VideoInfo>();

        if(cursor.moveToFirst()){
            do{
                VideoInfo info = new VideoInfo();

                info.setPath( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
                info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));

                //获取当前Video对应的Id，然后根据该ID获取其Thumb
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                String selection = MediaStore.Video.Thumbnails.VIDEO_ID +"=?";
                String[] selectionArgs = new String[]{
                        id+""
                };
                Cursor thumbCursor = ((Activity)context).managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs, null);

                if(thumbCursor.moveToFirst()){
                    info.setThumbPath( cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)));

                }

                //然后将其加入到videoList
                videoList.add(info);
            }while(cursor.moveToNext());
        }
        return videoList;

    }
}
