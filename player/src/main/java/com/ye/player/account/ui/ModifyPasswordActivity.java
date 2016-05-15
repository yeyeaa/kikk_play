package com.ye.player.account.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ye.player.R;
import com.ye.player.account.services.AccountService;
import com.ye.player.common.KikkPreferenceManager;
import com.ye.player.common.ui.activity.BaseActivity;
import com.ye.player.common.utils.StringUtil;
import com.ye.player.common.utils.Utils;

public class ModifyPasswordActivity extends BaseActivity {

	private EditText newpassword;
	private EditText oldpassword;
	private EditText confirmpassword;
	private AccountService accountService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_new_modify_password);
		setRightTextVisible(true);
		setRightTextColor(true);
		newpassword = (EditText) this.findViewById(R.id.modify_new_password_edittext);
		oldpassword = (EditText) this.findViewById(R.id.modify_old_password_edittext);
		confirmpassword = (EditText) this.findViewById(R.id.modify_confirm_password_edittext);

		accountService = new AccountService(ModifyPasswordActivity.this);

		setRightButton("保存", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String stroldPassword = oldpassword.getText().toString().trim();
				String strnewPassword = newpassword.getText().toString().trim();
				String strconfirmpassword = confirmpassword.getText().toString().trim();
				if (TextUtils.isEmpty(stroldPassword)) {
					Utils.showToast(ModifyPasswordActivity.this, R.string.modify_old_password_hint);
				} else if (TextUtils.isEmpty(strnewPassword)) {
					Utils.showToast(ModifyPasswordActivity.this, R.string.modify_new_password_hint);
				} else if (TextUtils.isEmpty(strconfirmpassword)) {
					Utils.showToast(ModifyPasswordActivity.this, R.string.modify_confirm_password_hint);
				} else if (!(strconfirmpassword.equals(strnewPassword))) {
					Utils.showToast(ModifyPasswordActivity.this, R.string.two_password);
				} else {
					updatePassword(stroldPassword, strnewPassword);
				}
			}
		});
		confirmpassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String stroldPassword = oldpassword.getText().toString().trim();
				String strnewPassword = newpassword.getText().toString().trim();
				if (TextUtils.isEmpty(stroldPassword) || TextUtils.isEmpty(strnewPassword)
						|| StringUtil.isEmpty(s.toString())) {
					setRightTextColor(true);
				} else {
					setRightTextColor(false);
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

	private void updatePassword(String oldPassword, String newPassword) {
		String pass = KikkPreferenceManager.getInstance().getPreferencesString("pass");
		if (pass == null){
			pass = "1";
		}
		if (oldPassword.equals(pass)){
			KikkPreferenceManager.getInstance().savePreferencesString("pass", newPassword);
			Utils.showToast(ModifyPasswordActivity.this , "修改成功");
			ModifyPasswordActivity.this.finish();
		} else {
			Utils.showToast(ModifyPasswordActivity.this , "旧密码错误");
		}

	}
}
