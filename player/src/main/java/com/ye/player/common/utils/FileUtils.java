package com.ye.player.common.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by yexiaoyou on 2016/5/5.
 */
public class FileUtils {
    public static boolean isFileExists(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return false;
        }
        return new File(filePath).exists();
    }

    public static boolean deleteFile(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return false;
        }
        return new File(filePath).delete();
    }

    public static File createDefaultCacheDir(Context paramContext) {
        File file = new File(paramContext.getApplicationContext().getCacheDir(), "picasso-cache");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String convertSize(long size){
        String mSize;
        if (size > 1024*1024*1024){
            mSize = size/(1024*1024*1024) + "G";
        }else if (size >1024*1024){
            mSize = size/(1024*1024) + "M";
        }else {
            mSize = size/(1024) + "KB";
        }
        return mSize;
    }
}
