package com.ye.player.account.ui;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ye.player.R;
import com.ye.player.account.bean.UserInfo;
import com.ye.player.account.services.UserInfoService;
import com.ye.player.common.ui.activity.BaseActivity;
import com.ye.player.common.utils.StringUtil;
import com.ye.player.common.utils.Utils;


public class BoundPhoneActivity extends BaseActivity  {

	private EditText phoneText;
	private UserInfoService userInfoService;
	private UserInfo user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bound_phone);
		userInfoService = new UserInfoService(this);
		init();
	}

	private void init() {
		setRightTextVisible(true);
		setRightTextColor(true);
		user = userInfoService.getCurrentUserInfo();
		setRightButton("存储", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String phone = phoneText.getEditableText().toString();
				if (StringUtil.isEmpty(phone)) {
					Utils.showToast(BoundPhoneActivity.this, "不能为空");
				} else if(phone.length() != 11){
					Utils.showToast(BoundPhoneActivity.this, "手机号错误");
				} else {
					updatePhone(phone);
				}
			}
		});

		if (user == null) {
			finish();
			return;
		}
		phoneText = (EditText) findViewById(R.id.edit_nick_name);
		if (!StringUtil.isEmpty(user.getPhone())) {
			phoneText.setHint(user.getPhone());
		}
		phoneText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (StringUtil.isEmpty(s.toString())) {
					setRightTextColor(false);
				} else {
					setRightTextColor(true);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void updatePhone(final String phone) {
		user.setPhone(phone);
		userInfoService.updateCurrentUserInfo(user);
		Utils.showToast(BoundPhoneActivity.this, "修改成功");
		BoundPhoneActivity.this.finish();
	}

}
