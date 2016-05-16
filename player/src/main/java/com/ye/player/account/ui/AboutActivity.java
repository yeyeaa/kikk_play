package com.ye.player.account.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ye.player.R;
import com.ye.player.ad.ui.CustomBrowserActivity;
import com.ye.player.common.ui.activity.BaseActivity;
import com.ye.player.common.utils.Utils;

public class AboutActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.check_update_relative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(AboutActivity.this, "暂无新版本");
            }
        });

        findViewById(R.id.temp).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, CustomBrowserActivity.class);
                intent.putExtra("url", "http://www.baidu.com");
                startActivity(intent);
            }
        });
    }
}
