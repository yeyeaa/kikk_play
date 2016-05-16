package com.ye.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.ye.player.R;


public class BrowserBottomNavigationBar extends RelativeLayout {

	public BrowserBottomNavigationBar(Context context) {
		super(context, null);
		initView(context);
	}

	public BrowserBottomNavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.web_app_browser_navigation_bar, null);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		view.setLayoutParams(lp);
		addView(view);
	}

	@Override
	public void setOnClickListener(OnClickListener listener) {
		findViewById(R.id.backward_btn).setOnClickListener(listener);
		findViewById(R.id.close_btn).setOnClickListener(listener);
		findViewById(R.id.refresh_btn).setOnClickListener(listener);
		findViewById(R.id.share_btn).setOnClickListener(listener);
	}

}
