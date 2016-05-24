package com.ye.player.ad.services;

import android.content.Context;

import com.ye.player.ad.bean.AdInfo;
import com.ye.player.common.bean.VideoInfo;

import java.util.ArrayList;
import java.util.List;

public class AdInfoService {
    private Context context;

    public AdInfoService(Context context) {
        this.context = context;
    }

    public List<AdInfo> getAds(VideoInfo videoInfo){


        //模拟数据
        List<AdInfo> list = new ArrayList<>();
        AdInfo  adInfo = new AdInfo();
        adInfo.setAdId("00001");
        adInfo.setStartTime(10000L);
        adInfo.setDuration(50000L);
        adInfo.setImageSoturce("www.baidu.com");
        adInfo.setLink("http://taobao.com/");
        list.add(adInfo);

       /* AdInfo  adInfo1 = new AdInfo();
        adInfo1.setAdId("00002");
        adInfo1.setStartTime(55000L);
        adInfo1.setDuration(5000L);
        adInfo1.setImageSoturce("www.baidu.com");
        adInfo1.setLink("http://www.baidu.com");
        list.add(adInfo1);*/

        return list;
    }
}
