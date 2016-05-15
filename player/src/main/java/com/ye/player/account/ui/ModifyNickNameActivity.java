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

public class ModifyNickNameActivity extends BaseActivity {

	private EditText nickName;
	private UserInfoService userInfoService;
	private UserInfo user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_nickname);
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
				String name = nickName.getEditableText().toString();
				if (StringUtil.isEmpty(name)) {
					Utils.showToast(ModifyNickNameActivity.this, "昵称不能为空");
				} else {
					byte[] nameBytes = name.getBytes();
					for (int i = 0; i < nameBytes.length; i += 3) {
						if ((nameBytes[i] & 0xF8) == 0xF0) {
							Utils.showToast(ModifyNickNameActivity.this, "昵称非法");
							return;
						}
					}
					updateNickname(name);
				}
			}
		});

		if (user == null) {
			finish();
			return;
		}
		nickName = (EditText) findViewById(R.id.edit_nick_name);
		if (!StringUtil.isEmpty(user.getNickName())) {
			nickName.setHint(user.getNickName());
		}
		nickName.addTextChangedListener(new TextWatcher() {
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

	private void updateNickname(final String nickName) {
		   user.setNickName(nickName);
           userInfoService.updateCurrentUserInfo(user);
		   Utils.showToast(ModifyNickNameActivity.this, "修改成功");
		   ModifyNickNameActivity.this.finish();
	}

}
