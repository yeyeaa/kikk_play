package com.ye.player.menu.ui.activity;

import android.os.Bundle;

import com.ye.player.R;
import com.ye.player.common.bean.VideoInfo;
import com.ye.player.common.service.VideoInfoService;
import com.ye.player.common.ui.BaseActivity;

import java.util.List;

public class MenuActivity extends BaseActivity {

    private VideoInfoService videoInfoService;

    private List<VideoInfo> list;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        videoInfoService = new VideoInfoService(this);
        init();
        initView();
    }

    private void init() {
        list = videoInfoService.getVideoInfo();
    }

    private void initView() {

    }

}
