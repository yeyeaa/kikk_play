package com.ye.player.splash.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.ye.player.R;
import com.ye.player.common.bean.VideoInfo;
import com.ye.player.common.service.VideoInfoService;
import com.ye.player.common.ui.BaseActivity;
import com.ye.player.menu.ui.activity.MenuActivity;

public class SplashActivity extends BaseActivity {

    private VideoInfoService videoInfoService;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        videoInfoService = new VideoInfoService(this);
        init();
    }

    private void init() {
        videoInfoService.updateVideoInfo(handler);
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //暂时延时执行
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, 2000);
        }
    };
}
