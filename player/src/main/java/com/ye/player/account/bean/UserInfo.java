package com.ye.player.account.bean;

import android.content.Context;

import java.lang.reflect.Field;

public class UserInfo {

	private String loginId;

	private String userId;

	private String nickName;

	private String recommendedCode;

	private Short gender;

	private String birthday;

	private String phone;

	private String stateDesc;

	private String userOrigin;

	public String getUserRegtime() {
		return userRegtime;
	}

	public void setUserRegtime(String userRegtime) {
		this.userRegtime = userRegtime;
	}

	private String userRegtime;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUserOrigin() {
		return userOrigin;
	}

	public void setUserOrigin(String userOrigin) {
		this.userOrigin = userOrigin;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRecommendedCode() {
		return recommendedCode;
	}

	public void setRecommendedCode(String recommendedCode) {
		this.recommendedCode = recommendedCode;
	}

	public Short getGender() {
		return gender;
	}

	public void setGender(Short gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public String getValueByKey(String key, Context context) {
		Class<? extends UserInfo> userCla = (Class<? extends UserInfo>) this.getClass();
		Field[] fs = userCla.getDeclaredFields();
		String value = null;
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true);
			Object val = null;
			try {
				val = f.get(this);
				if (key.equals(f.getName())) {
					value = (String) val;
					break;
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

}
