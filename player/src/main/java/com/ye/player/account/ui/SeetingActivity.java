package com.ye.player.account.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ye.player.R;
import com.ye.player.account.bean.UserInfo;
import com.ye.player.account.services.AccountService;
import com.ye.player.account.services.UserInfoService;
import com.ye.player.common.ui.activity.BaseActivity;
import com.ye.player.common.utils.StringUtil;
import com.ye.player.common.utils.Utils;

public class SeetingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout accountLayout, seetingLayout, aboutLayout, quitLayout;
    private AccountService accountService;
    private UserInfoService userInfoService;
    private UserInfo userInfo;

    private TextView textViewNick, textViewPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeting);
        accountService = new AccountService(this);
        userInfoService = new UserInfoService(this);
        initView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        userInfo = userInfoService.getCurrentUserInfo();
        initData();
    }

    private void initData() {
        if (userInfo != null){
            if (userInfo.getNickName() != null){
                textViewNick.setText(userInfo.getNickName());
            } else {
                textViewNick.setText("暂未设置昵称");
            }
            if (!StringUtil.isEmpty(userInfo.getPhone())&&userInfo.getPhone().length() == 11) {
                textViewPhone.setText(userInfo.getPhone().substring(0, 3) + "*****" + userInfo.getPhone().substring(8, 11));
            } else {
                textViewPhone.setText("暂未绑定手机号");
            }
        }
    }

    private void initView() {
        accountLayout = (RelativeLayout) findViewById(R.id.setting_rl_account);
        seetingLayout = (RelativeLayout) findViewById(R.id.setting_rl_lock);
        aboutLayout = (RelativeLayout) findViewById(R.id.about);
        quitLayout = (RelativeLayout) findViewById(R.id.quit);
        textViewNick = (TextView) findViewById(R.id.text_name) ;
        textViewPhone = (TextView) findViewById(R.id.text_number);

        accountLayout.setOnClickListener(this);
        seetingLayout.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);
        quitLayout.setOnClickListener(this);
    }


    @Override
    public boolean hasLeftBarButton() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_rl_account:{
                Intent intent = new Intent(SeetingActivity.this, MyAccountActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.setting_rl_lock:{
                Utils.showToast(SeetingActivity.this, "敬请期待");
                break;
            }
            case R.id.about:{
                Intent intent = new Intent(SeetingActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.quit:{
                if (accountService.logOut());
                SeetingActivity.this.finish();
                break;
            }

        }
    }
}
