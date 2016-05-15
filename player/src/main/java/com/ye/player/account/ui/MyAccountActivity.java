package com.ye.player.account.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ye.player.R;
import com.ye.player.account.bean.UserInfo;
import com.ye.player.account.services.UserInfoService;
import com.ye.player.common.ui.activity.BaseActivity;
import com.ye.player.common.ui.dialog.BlurDialog;
import com.ye.player.common.ui.dialog.CustomDialog;
import com.ye.player.common.utils.StringUtil;
import com.ye.player.widget.wheelwidget.OnWheelChangedListener;
import com.ye.player.widget.wheelwidget.WheelView;
import com.ye.player.widget.wheelwidget.adapter.ArrayWheelAdapter;

import java.util.Arrays;
import java.util.Calendar;

public class MyAccountActivity extends BaseActivity implements OnClickListener {
	private UserInfo user;
	private TextView gender;
	private TextView birthday;
	private Short sexShort;
	private String stringBirthday;
	private ImageView genderNotice, birthdayNoti;
	private GenderSettingDialog dialog;
	private UserInfoService userInfoService;
	private TextView phone;
	private CustomDialog customDialog = null, stateDialog = null, logoutDialog = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		userInfoService = new UserInfoService(this);
		init(this);
	}

	public void init(Context context) {
		phone = (TextView) findViewById(R.id.my_account_current_telephone);
		gender = (TextView) findViewById(R.id.my_account_gender);
		birthday = (TextView) findViewById(R.id.my_account_birthday);
		genderNotice = (ImageView) findViewById(R.id.gender_notice);
		birthdayNoti = (ImageView) findViewById(R.id.birthday_notice);
		findViewById(R.id.info_usbmit).setOnClickListener(this);
	}

	// 当屏幕聚焦的时候设置性别
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			sexShort = user.getGender();
			if (user.getGender() == null || sexShort == 0) {
				sexShort = 1;
				gender.setText(R.string.gender_setting_activity_label);
			} else if (sexShort == 1) {
				gender.setText(R.string.gender_male);
			} else if (sexShort == 2) {
				gender.setText(R.string.gender_female);
			}
			if (user.getBirthday() != null) {
				birthdayNoti.setVisibility(View.INVISIBLE);
			}
			if (user.getGender() != null) {
				genderNotice.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		user = userInfoService.getCurrentUserInfo();
		if (user == null){
			user =new UserInfo();
		}
		String nickName = user.getNickName();
		((TextView) findViewById(R.id.my_account_current_nickname))
				.setText(nickName == null ? getString(R.string.me_no_nick_name) + user.getRecommendedCode() : nickName);
		sexShort = user.getGender();
		if (sexShort == null) {
			sexShort = 1;
		}
		if (user.getGender() == null || sexShort == 0) {
			genderNotice.setVisibility(View.VISIBLE);
		} else if (sexShort == 1) {
			gender.setText(R.string.gender_male);
			genderNotice.setVisibility(View.INVISIBLE);
		} else if (sexShort == 2) {
			gender.setText(R.string.gender_female);
			genderNotice.setVisibility(View.INVISIBLE);
		}
		String phoneNo = user.getPhone();
		if (!StringUtil.isEmpty(user.getPhone())) {
			if (phoneNo.length() < 11) {
				phone.setText(phoneNo);
			} else {
				phone.setText(phoneNo.substring(0, 3) + "*****" + phoneNo.substring(8, 11));
			}
		} else {
			phone.setText(R.string.you_not_bound_phone_yet);
		}
		if (user.getBirthday() == null) {
			birthdayNoti.setVisibility(View.VISIBLE);
		} else if (user.getBirthday() != null) {
			String[] dateArr = user.getBirthday().split("-");
			birthday.setText(dateArr[0] + "年" + dateArr[1] + "月" + (dateArr.length == 3 ? dateArr[2] : "01") + "日");
			stringBirthday = dateArr[0] + "-" + dateArr[1] + 1 + "-" + (dateArr.length == 3 ? dateArr[2] : "01");
			birthdayNoti.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		if (customDialog != null) {
			customDialog.dismiss();
		}
		if (logoutDialog != null) {
			logoutDialog.dismiss();
		}
		if (stateDialog != null) {
			stateDialog.dismiss();
		}
		super.onDestroy();
	}

	public void goNicknameSetting(View view) {
		Intent intent = new Intent(this, ModifyNickNameActivity.class);
		startActivity(intent);
	}

	public void goGenderSetting(View view) {
		showChangeGenderDialog();
	}

	// 窗口弹出
	private void showChangeGenderDialog() {
		dialog = new GenderSettingDialog(MyAccountActivity.this, R.style.new_setting_dialog, sexShort);
		dialog.show();
	}

	public void goBoundPhone(View view) {
			Intent intent = new Intent(this, BoundPhoneActivity.class);
			startActivity(intent);
	}

	public void goBirthday(View view) {
		Dialog dialog = new BirthdayDialog(this, R.style.new_setting_dialog);
		dialog.show();
	}

	public void goModifyPassword(View view) {
			Intent intent = new Intent(this, ModifyPasswordActivity.class);
			startActivity(intent);

	}


	public class BirthdayDialog extends BlurDialog implements OnClickListener, OnWheelChangedListener {
		private WheelView yearWheel;
		private WheelView monthWheel;
		private WheelView dayWheel;
		private Button confirmBtn;
		private Button cancelBtn;
		private String[] years;
		private String[] months;
		private String[] days;
		private String[] dayMonth;
		private OnTaskInterface callbackProxy;
		private DateArrayAdapter yearAdapter, monthAdapter, dayAdapter;
		private int newYearValue = 0, newMonthValue = 0;
		int defaultYear;
		int defaultMonth;
		int defaultDay;

		public BirthdayDialog(Context context, int theme) {
			super(context, theme);
		}

		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.dialog_birthday);
			init();
		}

		@SuppressLint("NewApi")
		private void init() {
			String birthDay = userInfoService.getCurrentUserInfo().getBirthday();
			if (StringUtil.isEmpty(birthDay) || birthDay.split("-").length != 3) {
				defaultYear = 1900;
				defaultMonth = 1;
				defaultDay = 1;
			} else {
				String birthStrArr[] = birthDay.split("-");
				defaultYear = Integer.parseInt(birthStrArr[0]);
				defaultMonth = Integer.parseInt(birthStrArr[1]);
				defaultDay = Integer.parseInt(birthStrArr[2]);
			}

			confirmBtn = (Button) findViewById(R.id.birthday_dialog_btn_confirm);
			confirmBtn.setOnClickListener(this);
			cancelBtn = (Button) findViewById(R.id.birthday_dialog_btn_cancel);
			cancelBtn.setOnClickListener(this);
			years = getContext().getResources().getStringArray(R.array.years);
			months = getContext().getResources().getStringArray(R.array.months);
			days = getContext().getResources().getStringArray(R.array.days);
			yearWheel = (WheelView) findViewById(R.id.birthday_dialog_year);
			yearAdapter = new DateArrayAdapter(getContext(), Gravity.LEFT, years, defaultYear - 1900);
			yearWheel.setViewAdapter(yearAdapter);
			yearWheel.setCurrentItem(defaultYear - 1900);
			yearWheel.setCyclic(true);
			yearWheel.addChangingListener(this);

			monthWheel = (WheelView) findViewById(R.id.birthday_dialog_month);
			monthAdapter = new DateArrayAdapter(getContext(), Gravity.CENTER, months, defaultMonth - 1);
			monthWheel.setViewAdapter(monthAdapter);
			monthWheel.setCurrentItem(defaultMonth - 1);
			monthWheel.setCyclic(true);
			monthWheel.addChangingListener(this);

			Calendar time = Calendar.getInstance();
			time.clear();
			time.set(Calendar.YEAR, defaultYear);
			time.set(Calendar.MONTH, defaultMonth - 1);
			int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);
			dayMonth = Arrays.copyOfRange(days, 0, day);

			dayWheel = (WheelView) findViewById(R.id.birthday_dialog_day);
			dayAdapter = new DateArrayAdapter(getContext(), Gravity.CENTER, dayMonth, defaultDay - 1);
			dayWheel.setViewAdapter(dayAdapter);
			dayWheel.setCurrentItem(defaultDay - 1);
			dayWheel.setCyclic(true);
			dayWheel.addChangingListener(this);

			callbackProxy = new OnTaskInterface() {
				@Override
				public void onTaskCompleted(boolean isSuccess) {
					if (isSuccess) {
						String stringYear = years[yearWheel.getCurrentItem()];
						String stringMonth = months[monthWheel.getCurrentItem()];
						String stringDay = dayMonth[dayWheel.getCurrentItem()];
						birthday.setText(stringYear + "年" + stringMonth + "月" + stringDay + "日");
						birthdayNoti.setVisibility(View.INVISIBLE);
					}
					dismiss();
				}
			};
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.birthday_dialog_btn_cancel:
					this.dismiss();
					break;
				case R.id.birthday_dialog_btn_confirm:
					String stringYear = years[yearWheel.getCurrentItem()];
					String stringMonth = months[monthWheel.getCurrentItem()];
					String stringDay = dayMonth[dayWheel.getCurrentItem()];
					showWaitDialog(getString(R.string.common_string_wait_notice));
					updateBirthday(stringYear + "-" + stringMonth + "-" + stringDay, callbackProxy);
					break;
			}
		}

		@Override
		public void onChanged(WheelView wheel, int oldValue, final int newValue) {
			switch (wheel.getId()) {
				case R.id.birthday_dialog_year:
					newYearValue = newValue;
					yearAdapter.setCurrentValue(newValue);
					yearWheel.invalidateWheel(true);
					break;
				case R.id.birthday_dialog_month:
					newMonthValue = newValue;
					monthAdapter.setCurrentValue(newValue);
					monthWheel.invalidateWheel(true);

					new Handler(getMainLooper()).post(new Runnable() {
						@SuppressLint("NewApi")
						@Override
						public void run() {
							Calendar time = Calendar.getInstance();
							time.clear();
							if (newYearValue != 0 && newYearValue != defaultYear) {
								time.set(Calendar.YEAR, Integer.parseInt(years[newYearValue]));
							} else {
								time.set(Calendar.YEAR, defaultYear);
							}
							time.set(Calendar.MONTH, newMonthValue);
							int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);
							dayMonth = Arrays.copyOfRange(days, 0, day);

							dayAdapter = new DateArrayAdapter(getContext(), Gravity.CENTER, dayMonth, defaultDay - 1);
							dayWheel.setViewAdapter(dayAdapter);
							dayWheel.setCurrentItem(defaultDay - 1);

						}
					});
					break;
				case R.id.birthday_dialog_day:
					dayAdapter.setCurrentValue(newValue);
					dayWheel.invalidateWheel(true);
					break;
			}
		}

		private class DateArrayAdapter extends ArrayWheelAdapter<String> {
			int gravity;
			int currentItem;
			int currentValue;

			public DateArrayAdapter(Context context, int gravity, String[] items, int current) {
				super(context, items);
				this.currentValue = current;
				this.gravity = gravity;
				setTextSize(getResources().getDimensionPixelSize(R.dimen.custom_dialog_text_title_text_size_birthday));
			}

			public void setCurrentValue(int current) {
				this.currentValue = current;
			}

			@Override
			protected void configureTextView(TextView view) {
				super.configureTextView(view);
				view.setTypeface(Typeface.SANS_SERIF);
				view.setGravity(gravity);
				view.setPadding(0, 5, 5, 5);
				if (currentItem == currentValue) {
					view.setTextColor(getResources().getColor(R.color.main_red));
				} else {
					view.setTextColor(Color.GRAY);
				}
			}

			@Override
			public View getItem(int index, View cachedView, ViewGroup parent) {
				currentItem = index;
				TextView view = (TextView) super.getItem(index, cachedView, parent);
				configureTextView(view);
				return view;
			}
		}
	}

	private void updateBirthday(String birthday, final OnTaskInterface listener) {
		dismissWaitDialog();
		user.setBirthday(birthday);
		userInfoService.updateCurrentUserInfo(user);
		listener.onTaskCompleted(true);
	}

	public interface OnTaskInterface {
		void onTaskCompleted(boolean isSuccess);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.info_usbmit:
				if (StringUtil.isEmpty(stringBirthday)) {
					stringBirthday = "1900-01-01";
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 设置性别的弹窗
	 *
	 * @author MaChi
	 */
	public class GenderSettingDialog extends BlurDialog {
		private ImageView maleRadio;
		private ImageView femaleRadio;

		public GenderSettingDialog(Context context, int theme, short sex_status) {
			super(context, theme);
			sexShort = sex_status;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.change_gender_dialog);
			maleRadio = (ImageView) findViewById(R.id.gender_setting_im_male);
			femaleRadio = (ImageView) findViewById(R.id.gender_setting_im_female);
			RelativeLayout maleLayout = (RelativeLayout) findViewById(R.id.gender_setting_rl_male_m);
			RelativeLayout femaleLayout = (RelativeLayout) findViewById(R.id.gender_setting_rl_female_f);
			if (sexShort == 2) {
				femaleRadio.setVisibility(View.VISIBLE);
				maleRadio.setVisibility(View.GONE);
			} else {
				maleRadio.setVisibility(View.VISIBLE);
				femaleRadio.setVisibility(View.GONE);
			}
			maleLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					maleRadio.setVisibility(View.VISIBLE);
					femaleRadio.setVisibility(View.GONE);
					sexShort = 1;
					updateGender(sexShort);
					dialog.dismiss();

				}
			});
			femaleLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					femaleRadio.setVisibility(View.VISIBLE);
					maleRadio.setVisibility(View.GONE);
					sexShort = 2;
					updateGender(sexShort);
					dialog.dismiss();
				}
			});
		}
	}

	private void updateGender(Short gender) {
		user.setGender(gender);
		userInfoService.updateCurrentUserInfo(user);
	}

}
