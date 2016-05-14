package com.ye.player.common.utils;

import android.content.Context;

import com.ye.player.KikkApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static void storeJSON(String fileName, Object data, boolean encrypt) {
        try {
            String dataStr = JSONUtil.toJSONString(data);
            storeString(fileName, dataStr, encrypt);
        } catch (Exception e) {
        }
    }

    public static <T> T readJSON(String fileName, boolean encrypt, Class<T> cls) {
        try {
            String dataStr = readString(fileName, encrypt);
            return JSONUtil.parseObject(dataStr, cls);
        } catch (Exception e) {
        }
        return null;
    }

    public static String readString(String fileName, boolean encrypt) {
        FileInputStream inputStream = null;
        try {
            inputStream = KikkApplication.app.getApplicationContext().openFileInput(fileName);
            if (inputStream != null) {
                String data = new String(ByteStreams.toByteArray(inputStream));
                if (encrypt) {
                    data = AESTools.decode(data);
                }
                return data;
            }
        } catch (Exception e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static void storeString(String fileName, String data, boolean encrypt) {
        FileOutputStream outStream = null;
        try {
            if (encrypt) {
                data = AESTools.encode(data);
            }
            outStream = KikkApplication.app.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outStream.write(data.getBytes());
        } catch (Exception e) {
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
