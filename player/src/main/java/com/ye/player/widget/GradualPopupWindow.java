package com.ye.player.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.ye.player.R;


public class GradualPopupWindow {
	private PopupWindow popupWindow;
	private boolean isBusy = false;
	private Context context;
	private RelativeLayout contentView;
	private View userView;
	private Animation inAni;
	private Animation outAni;

	public GradualPopupWindow(Context context) {
		this.context = context;
		init();
	}

	public void setOnDismissListener(OnDismissListener dismissListener) {
		if (popupWindow != null) {
			popupWindow.setOnDismissListener(dismissListener);
		}
	}

	private void init() {
		contentView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_popupwindow, null);
		popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setOutsideTouchable(true);
		contentView.setFocusableInTouchMode(true);
		contentView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				dismiss();
				return true;
			}
		});
		contentView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK) {
					if (popupWindow != null) {
						dismiss();
					}
				}

				return false;
			}
		});
		inAni = AnimationUtils.loadAnimation(context, R.anim.push_down_in);
		outAni = AnimationUtils.loadAnimation(context, R.anim.push_down_out);
	}

	public View setContentView(View contentView) {
		if (this.contentView != null) {
			this.contentView.removeAllViews();
			this.contentView.addView(contentView);
			this.userView = this.contentView.getChildAt(0);
		}
		return userView;
	}

	public View setContentView(int layoutId) {
		if (this.contentView != null) {
			this.contentView.removeAllViews();
			this.userView = LayoutInflater.from(context).inflate(layoutId, this.contentView, false);
			this.contentView.addView(userView);
		}
		return userView;
	}

	public void setInAnimation(Animation in) {
		inAni = in;
	}

	public void setOutAnimation(Animation out) {
		outAni = out;
	}

	public void show(final View parent, int gravity, int x, int y) {
		if (popupWindow != null && contentView != null && !isBusy) {
			if (gravity == Gravity.NO_GRAVITY) {
				gravity = Gravity.TOP | Gravity.RIGHT;
			}
			LayoutParams contentLayoutParams = (LayoutParams) userView.getLayoutParams();
			if (contentLayoutParams == null) {
				contentLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			}
			contentLayoutParams.addRule(gravity, RelativeLayout.TRUE);
			contentLayoutParams.leftMargin = x;
			contentLayoutParams.topMargin = y;
			userView.setLayoutParams(contentLayoutParams);
			if (inAni != null) {
				inAni.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {
						isBusy = true;
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {

					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						isBusy = false;
					}
				});
				if (userView != null) {
					userView.startAnimation(inAni);
				}
			}
			popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
		}
	}

	public void dismiss() {
		try {
			if (popupWindow != null && popupWindow.isShowing() && !isBusy) {
				if (outAni != null) {
					outAni.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation arg0) {
							isBusy = true;
						}

						@Override
						public void onAnimationRepeat(Animation arg0) {

						}

						@Override
						public void onAnimationEnd(Animation arg0) {
							userView.post(new Runnable() { // 我不知道为什么，但对于某些手机必须用post
								@Override
								public void run() {
									popupWindow.dismiss();
								}
							});
							isBusy = false;
						}
					});
					if (userView != null) {
						userView.startAnimation(outAni);
					}
				} else {
					userView.post(new Runnable() { // 我不知道为什么，但对于某些手机必须用post
						@Override
						public void run() {
							popupWindow.dismiss();
						}
					});
					isBusy = false;
				}
			}
		} catch (Exception e) {
		}
	}
}
