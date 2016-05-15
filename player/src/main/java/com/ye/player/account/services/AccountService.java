package com.ye.player.account.services;

import android.content.Context;

import com.ye.player.common.Constants;
import com.ye.player.common.KikkPreferenceManager;

public class AccountService {

	private Context context;

	public AccountService(Context context) {
		this.context = context;
	}

	public boolean logIn(String name,String password){
		String pass = KikkPreferenceManager.getInstance().getPreferencesString("pass");
		if (pass == null){
			pass = "1";
		}
		if (name.equals("1") && password.equals(pass)) {
			KikkPreferenceManager.getInstance().savePreferencesBoolean(Constants.LOG_IN, true);
			return true;
		}
		return false;
	}

	public boolean logOut(){
		KikkPreferenceManager.getInstance().savePreferencesBoolean(Constants.LOG_IN, false);
		return true;
	}

	public boolean isLogIn(){
		return KikkPreferenceManager.getInstance().getPreferencesBoolean(Constants.LOG_IN, false);
	}

}
