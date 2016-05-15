package com.ye.player.common.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.ye.player.common.utils.BlurUtils;
import com.ye.player.common.utils.Utils;

public class BlurDialog extends Dialog {

	private boolean isBlur = false;
	private Activity activity;

	public BlurDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public BlurDialog(Context context) {
		super(context);
		init(context);
	}

	protected BlurDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	private void init(Context context) {
		setOwnerActivity((Activity) context);
		activity = getActivity();
	}

	@Override
	public void show() {
		super.show();
		if (isBlur) {
			BlurUtils.applyBlur(activity, 2, 2);
		}
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (Utils.getDeviceWidth(getContext()) * 0.9);
		getWindow().setAttributes(lp);
	}

	@Override
	public void dismiss() {
		if (isBlur) {
			BlurUtils.cancelBlur(activity);
		}
		super.dismiss();
	}

	public void setBlurEnable(boolean isBlur) {
		this.isBlur = isBlur;
	}

	private Activity getActivity() {
		Activity activity = getOwnerActivity();
		while (true) {
			if (activity.getParent() == null) {
				return activity;
			} else {
				activity = activity.getParent();
			}
		}
	}
}
