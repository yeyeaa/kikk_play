package com.ye.player.menu.ui.activity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.ye.player.R;
import com.ye.player.common.bean.VideoInfo;
import com.ye.player.common.service.VideoInfoService;
import com.ye.player.common.ui.activity.BaseFragmentActivity;
import com.ye.player.menu.ui.fragment.MenuFragment;

import java.util.List;

public class MenuActivity extends BaseFragmentActivity {
    private Fragment menuFragment;

  /*  private VideoInfoService videoInfoService;

    private List<VideoInfo> list;*/

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        menuFragment = new MenuFragment();
        transaction.add(R.id.content, menuFragment);
        transaction.commit();

       /* videoInfoService = new VideoInfoService(this);
        init();
        initView();*/
    }

  /*  private void init() {
        list = videoInfoService.getVideoInfo();
    }

    private void initView() {

    }*/

    public boolean hasLeftBarButton() {
        return false;
    }
}
