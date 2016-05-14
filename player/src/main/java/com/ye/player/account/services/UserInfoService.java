package com.ye.player.account.services;

import android.content.Context;

import com.ye.player.account.bean.UserInfo;
import com.ye.player.common.utils.FileUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserInfoService {

	private static final ReadWriteLock userInfoLock = new ReentrantReadWriteLock();

	private static final Lock userInfoReadLock = userInfoLock.readLock();

	private static final Lock userInfoWriteLock = userInfoLock.writeLock();

	private static volatile UserInfo userInfo = null;

	private static volatile boolean userInfoLoaded = false;

	private static final String USER_INFO_FILE = ".uis.uif";

	public UserInfoService(Context context) {
	}

	public UserInfo getCurrentUserInfo() {
		userInfoReadLock.lock();
		try {
			if (userInfo == null && !userInfoLoaded) {
				synchronized (userInfoLock) {
					if (userInfo == null && !userInfoLoaded) {
						userInfo = FileUtils.readJSON(USER_INFO_FILE, true, UserInfo.class);
						userInfoLoaded = true;
					}
				}
			}
		} finally {
			userInfoReadLock.unlock();
		}
		return userInfo;
	}

	public void updateCurrentUserInfo(UserInfo userInfo) {
		userInfoWriteLock.lock();
		try {
			if (userInfo != null) {
				this.userInfo = userInfo;
			} else {
				if (this.userInfo != null) {
					this.userInfo.setLoginId(null);
				}
			}
			FileUtils.storeJSON(USER_INFO_FILE, userInfo, true);
		} finally {
			userInfoWriteLock.unlock();
		}
	}

	public String getLoginId() {
		UserInfo userInfo = getCurrentUserInfo();
		if (userInfo != null) {
			return userInfo.getLoginId();
		}
		return null;
	}

	public String getUserId() {
		UserInfo userInfo = getCurrentUserInfo();
		if (userInfo != null) {
			return userInfo.getUserId();
		}
		return null;
	}

	public String getPhone() {
		UserInfo userInfo = getCurrentUserInfo();
		if (userInfo != null) {
			return userInfo.getPhone();
		}
		return null;
	}
}
