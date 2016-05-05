package com.ye.player.datebase.helper;

import com.ye.player.common.bean.VideoInfo;
import com.ye.player.datebase.dao.VideoInfoDao;

public class DaoFactory {
    private volatile static VideoInfoDao videoInfoDao = null;

    public static VideoInfoDao getVideoInfoDao() {
        if (videoInfoDao != null) {
            return videoInfoDao;
        }
        synchronized (VideoInfoDao.class) {
            if (videoInfoDao != null) {
                return videoInfoDao;
            }
            videoInfoDao = new VideoInfoDao();
            return videoInfoDao;
        }
    }

}
