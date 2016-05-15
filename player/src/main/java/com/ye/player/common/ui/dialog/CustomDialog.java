package com.ye.player.common.ui.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ye.player.R;


public class CustomDialog extends BlurDialog {

	private Activity activity;
	private TextView title;
	private TextView subtitle;
	private LinearLayout body;
	private LinearLayout expand;
	private Button confirm;
	private Button confirm_ok, confirm_cancel;
	private Button addButton;
	private LinearLayout doubleBtns;

	private boolean IsSingle = true;

	public CustomDialog(Activity activity) {
		super(activity, R.style.new_setting_dialog);
		this.activity = activity;
	}

	public CustomDialog(Activity activity, boolean IsSingle) {
		super(activity, R.style.new_setting_dialog);
		this.activity = activity;
		this.IsSingle = IsSingle;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_custom);
		init();
	}

	private void init() {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		findViewById(R.id.dialog_container).getLayoutParams().width = ((int) (dm.widthPixels * 0.9));
		title = (TextView) findViewById(R.id.title);
		subtitle = (TextView) findViewById(R.id.subtitle);
		body = (LinearLayout) findViewById(R.id.body);
		expand = (LinearLayout) findViewById(R.id.expand_container);
		confirm = (Button) findViewById(R.id.confirm);
		doubleBtns = (LinearLayout) findViewById(R.id.dialog_custom_style_double_btns);
		confirm_ok = (Button) findViewById(R.id.confirm_ok);
		confirm_cancel = (Button) findViewById(R.id.confirm_cancel);
		addButton = (Button) findViewById(R.id.add_btn);

		if (IsSingle) {
			confirm.setVisibility(View.VISIBLE);
			doubleBtns.setVisibility(View.GONE);
		} else {
			confirm.setVisibility(View.GONE);
			doubleBtns.setVisibility(View.VISIBLE);
		}
	}

	public CustomDialog setCustomBackgroud(int resId) {
		title.setTextColor(activity.getResources().getColor(R.color.white));
		subtitle.setTextColor(activity.getResources().getColor(R.color.white));
		findViewById(R.id.header).setBackgroundResource(resId);
		findViewById(R.id.footer).setBackgroundResource(resId);
		return this;
	}

	public CustomDialog setBackgroundResource(int resource) {
		confirm.setBackgroundResource(resource);
		return this;
	}

	public CustomDialog setCustomTitle(int resId) {
		return setCustomTitle(activity.getString(resId));
	}

	public CustomDialog setCustomTitle(String text) {
		title.setText(text);
		title.setVisibility(View.VISIBLE);
		return this;
	}

	public CustomDialog setCustomTitle(String text, int gravity, boolean hasLine) {
		title.setText(text);
		title.setGravity(gravity);
		title.setVisibility(View.VISIBLE);
		if (hasLine) {
			findViewById(R.id.divider_line).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.divider_line).setVisibility(View.INVISIBLE);
		}
		return this;
	}

	public CustomDialog setCustomSubtitle(int resId) {
		return setCustomSubtitle(activity.getString(resId));
	}

	public CustomDialog setCustomSubtitle(String text) {
		subtitle.setText(text);
		subtitle.setVisibility(View.VISIBLE);
		return this;
	}

	public CustomDialog setCustomMessage(int resId) {
		return setCustomMessage(activity.getString(resId));
	}

	public CustomDialog setCustomMessage(String message) {
		TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.dialog_custom_textview, null);
		tv.setLineSpacing(1f,1.3f);
		tv.setText(message);
		tv.setTextColor(activity.getResources().getColor(R.color.text_black));
		return setBody(tv);
	}

	public CustomDialog setCustomUploadMessage(String message, int start, int stop) {
		TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.dialog_custom_textview, null);
		SpannableStringBuilder spanBuilder = new SpannableStringBuilder(message);
		spanBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, stop, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tv.setText(spanBuilder);
		return setBody(tv);
	}

	public CustomDialog setCustomColorMessage(Spanned message) {
		TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.dialog_custom_textview, null);
		tv.setText(message);
		tv.setTextColor(activity.getResources().getColor(R.color.text_black));
		return setBody(tv);
	}

	public CustomDialog setCustomColorMessage(SpannableStringBuilder message) {
		TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.dialog_custom_textview, null);
		tv.setText(message);
		tv.setTextColor(activity.getResources().getColor(R.color.text_black));
		return setBody(tv);
	}

	public CustomDialog setBody(View view) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		body.addView(view, lp);
		return this;
	}

	public CustomDialog setExpand(View view) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		expand.addView(view, lp);
		expand.setVisibility(View.VISIBLE);
		return this;
	}

	public CustomDialog setButton_OK_OnClickListener(String text, View.OnClickListener l) {
		confirm_ok.setText(text);
		confirm_ok.setOnClickListener(l);
		return this;
	}

	public CustomDialog setButton_Cancel_OnClickListener(String text, View.OnClickListener l) {
		confirm_cancel.setText(text);
		confirm_cancel.setOnClickListener(l);
		return this;
	}

	public CustomDialog setButton_OK_OnClickListener(int resId, View.OnClickListener l) {
		confirm_ok.setText(activity.getResources().getString(resId));
		confirm_ok.setOnClickListener(l);
		return this;
	}

	public CustomDialog setButton_Cancel_OnClickListener(int resId, View.OnClickListener l) {
		confirm_cancel.setText(activity.getResources().getString(resId));
		confirm_cancel.setOnClickListener(l);
		return this;
	}

	public CustomDialog setButton_Add_Btn_OnClickListener(String text, View.OnClickListener l) {
		addButton.setVisibility(View.VISIBLE);
		addButton.setText(text);
		addButton.setOnClickListener(l);
		return this;
	}

	public void setButton_Add_Btn_ClickAble(Boolean clickableBoolean) {
		addButton.setClickable(clickableBoolean);
	}

	public void setButton_Btn_OK_ClickAble(Boolean clickableBoolean) {
		confirm_ok.setClickable(clickableBoolean);
	}

	public CustomDialog setConfirmButtonOnClickListener(int resId, View.OnClickListener l) {
		confirm.setText(activity.getResources().getString(resId));
		confirm.setOnClickListener(l);
		return this;
	}

	public CustomDialog setConfirmButtonOnClickListener(String text, View.OnClickListener l) {
		confirm.setText(text);
		confirm.setOnClickListener(l);
		return this;
	}
}
