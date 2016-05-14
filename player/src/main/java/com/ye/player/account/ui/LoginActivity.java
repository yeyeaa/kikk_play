package com.ye.player.account.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ye.player.R;
import com.ye.player.account.services.AccountService;
import com.ye.player.common.ui.activity.BaseActivity;
import com.ye.player.common.utils.Utils;


public class LoginActivity extends BaseActivity implements OnClickListener, OnEditorActionListener {

	private EditText usernameEditText;
	private EditText passwordEditText;
	private AccountService accountService;
	private RelativeLayout deleteUsernameLayout, deletePasswordLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void doLogin() {
		String phoneNum = usernameEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNum)) {
			Utils.showToast(this, R.string.input_name);
		} else if (TextUtils.isEmpty(password)) {
			Utils.showToast(this, R.string.input_password);
		} else {
			startLogin(phoneNum, password);
		}
	}

	private void init() {
		accountService = new AccountService(this);
		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		findViewById(R.id.land).setOnClickListener(this);
		//findViewById(R.id.reg).setOnClickListener(this);
		//findViewById(R.id.forgot_password).setOnClickListener(this);
		deleteUsernameLayout = (RelativeLayout) findViewById(R.id.delete_username);
		deletePasswordLayout = (RelativeLayout) findViewById(R.id.delete_pwd);
		usernameEditText.addTextChangedListener(new Watcher(usernameEditText));
		passwordEditText.addTextChangedListener(new Watcher(passwordEditText));
		passwordEditText.setOnEditorActionListener(this);
		deleteUsernameLayout.setOnClickListener(this);
		deletePasswordLayout.setOnClickListener(this);
	}

	private void startLogin(final String phoneNum, String password) {
		if (accountService.logIn(phoneNum, password)){
			Intent intent = new Intent(LoginActivity.this, SeetingActivity.class);
			startActivity(intent);
			this.finish();
		} else {
			Utils.showToast(LoginActivity.this, R.string.input_error);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.land:
				doLogin();
				break;
			/*case R.id.reg:
				Utils.showToast("暂未开放");
				break;
			case R.id.forgot_password:
				Utils.showToast("暂未开放");
				break;*/
			case R.id.delete_username:
				showUsername(false);
				usernameEditText.setText("");
				break;
			case R.id.delete_pwd:
				showUserPwd(false);
				passwordEditText.setText("");
				break;
			default:
				break;
		}
	}


	public class Watcher implements TextWatcher {
		private EditText editID = null;

		public Watcher(EditText id) {
			editID = id;
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (editID == usernameEditText) {
				if (s.length() == 0) {
					showUsername(false);
				} else {
					showUsername(true);
				}
			} else if (editID == passwordEditText) {
				if (s.length() == 0) {
					showUserPwd(false);
				} else {
					showUserPwd(true);
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
	}

	private void showUsername(boolean show_username) {
		if (show_username) {
			if (!deleteUsernameLayout.isShown()) {
				deleteUsernameLayout.setVisibility(View.VISIBLE);
			}
		} else {
			if (deleteUsernameLayout.isShown()) {
				deleteUsernameLayout.setVisibility(View.GONE);
			}
		}
	}

	private void showUserPwd(boolean show_userpwd) {
		if (show_userpwd) {
			if (!deletePasswordLayout.isShown()) {
				deletePasswordLayout.setVisibility(View.VISIBLE);
			}
		} else {
			if (deletePasswordLayout.isShown()) {
				deletePasswordLayout.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public boolean hasToolBar() {
		return false;
	}

	@Override
	public boolean hasNavigationBar() {
		return false;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			doLogin();
		}
		return false;
	}

}
