package com.ye.player.common.utils;

/**
 * Created by yexiaoyou on 2016/5/10.
 */
public class TimeUtils {
    public static String longToString(long time){
        StringBuilder sb = new StringBuilder();
        time = time / 1000;
        int s = (int) (time % 60);
        int m = (int) (time / 60 % 60);
        int h = (int) (time / 3600);
        if (h != 0){
            sb.append(String.format("%02d",h)).append(":");
        }
        sb.append(String.format("%02d",m)).append(":");
        sb.append(String.format("%02d",s));
        return sb.toString();
    }
}
