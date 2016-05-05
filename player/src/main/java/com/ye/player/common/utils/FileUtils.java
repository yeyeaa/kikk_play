package com.ye.player.common.utils;

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
}
