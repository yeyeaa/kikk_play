package com.ye.player.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.ye.player.R;

public class NavigationBar extends RelativeLayout {

private Context context;

public TextView title;

private ImageButton leftBtn;

private ImageButton rightBtn;

public TextView rightText;

private Paint paint;

private boolean showBackground = false;

private ImageView titleImg;

private LinearLayout titleViewParentLinearLayout;

private RelativeLayout backgroundLayout;

public NavigationBar(Context context) {
        super(context);
        init(context);
        }

public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        }

private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.navigation_bar, this);
        title = (TextView) findViewById(R.id.navi_bar_title);
        leftBtn = (ImageButton) findViewById(R.id.navi_bar_left_btn);
        rightBtn = (ImageButton) findViewById(R.id.navi_bar_right_btn);
        rightText = (TextView) findViewById(R.id.navi_bar_right_text);
        titleImg = (ImageView) findViewById(R.id.navi_bar_title_img);
        backgroundLayout = (RelativeLayout) findViewById(R.id.navi_bar_background);
        titleViewParentLinearLayout = (LinearLayout) findViewById(R.id.navi_bar_title_parent_view);
        paint = new Paint();
        }

private void blurBackground(Canvas canvas, int width, int height) {
        paint.setColor(Color.argb(0x99, 0xff, 0xff, 0xff));
        canvas.drawRect(0, 0, width, height, paint);
        paint.setColor(Color.rgb(0xB2, 0xB2, 0xB2));
        paint.setStrokeWidth(1.0f);
        canvas.drawLine(0, height - 1, width, height - 1, paint);

        }

@Override
protected void dispatchDraw(Canvas canvas) {
        if (showBackground) {
final int width = getMeasuredWidth();
final int height = getMeasuredHeight();
        blurBackground(canvas, width, height);
        }
        super.dispatchDraw(canvas);
        }

public void setTitle(CharSequence title) {
        this.title.setText(title);
        }

public void setTitleColor(int resId) {
        this.title.setTextColor(resId);
        }

public void setLeftButton(int resId) {
        this.leftBtn.setVisibility(View.VISIBLE);
        this.leftBtn.setImageResource(resId);
        }

public void setLeftButton(OnClickListener l) {
        this.leftBtn.setVisibility(View.VISIBLE);
        this.leftBtn.setImageResource(R.drawable.navi_bar_back_img_white);
        this.leftBtn.setOnClickListener(l);
        }

public void setLeftButton(int resId, OnClickListener l) {
        this.leftBtn.setVisibility(View.VISIBLE);
        this.leftBtn.setImageResource(resId);
        this.leftBtn.setOnClickListener(l);
        }

public void setRightButton(int resId, OnClickListener l) {
        this.rightBtn.setVisibility(View.VISIBLE);
        this.rightBtn.setImageResource(resId);
        this.rightBtn.setOnClickListener(l);
        }

public ImageButton getRightBtn() {
        return rightBtn;
        }

public void setRightButton(int resid) {
        this.rightBtn.setVisibility(View.VISIBLE);
        this.rightBtn.setImageResource(resid);
        }

public void setTitleImage(int resid) {
        this.title.setVisibility(View.GONE);
        this.titleImg.setVisibility(View.VISIBLE);
        this.titleImg.setBackgroundResource(resid);
        }

public void setTitleAlpha(float alpha) {
        ViewHelper.setAlpha(this.title, alpha);
        }

public void setTitleImageAlpha(float alpha) {
        ViewHelper.setAlpha(this.titleImg, alpha);
        }

public void setBackgroundLayout(int resid) {
        this.leftBtn.setBackgroundResource(resid);
        this.backgroundLayout.setBackgroundResource(resid);
        }

public void setRightButtonVisible(boolean visible) {
        if (visible) {
        this.rightBtn.setVisibility(View.VISIBLE);
        } else {
        this.rightBtn.setVisibility(View.INVISIBLE);
        }
        }

public void setRightText(OnClickListener l) {
        this.rightText.setOnClickListener(l);
        }

public void setRightText(String value, OnClickListener l) {
        this.rightText.setOnClickListener(l);
        this.rightText.setText(value);
        }

public void setBackgroundEnabl(boolean enabled) {
        showBackground = enabled;
        invalidate();
        }

public void setRightTextVisible(boolean visible) {
        if (visible) {
        this.rightText.setVisibility(View.VISIBLE);
        } else {
        this.rightText.setVisibility(View.INVISIBLE);
        }
        }

public void setRightTextBackground(boolean visible) {
        if (visible) {
        this.rightText.setTextColor(getResources().getColor(R.color.white));
        } else {
        this.rightText.setTextColor(getResources().getColor(R.color.white_));
        }
        }

public void setRightText(CharSequence str) {
        this.rightText.setText(str);
        }

public void setCustomTitleView(View view) {
        titleViewParentLinearLayout.removeAllViews();
        titleViewParentLinearLayout.addView(view);
        }


public void setAlpha(float alpha ) {
        backgroundLayout.setAlpha(alpha);
}

}
