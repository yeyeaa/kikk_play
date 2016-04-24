package com.ye.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ye.player.R;

public class CommonNoticeView extends RelativeLayout {

    private ImageView image;

    private TextView text;

    public CommonNoticeView(Context context) {
        super(context);
        init(context);
    }

    public CommonNoticeView(Context context, int bgResId) {
        super(context);
        init(context);
        findViewById(R.id.common_notice_container).setBackgroundResource(bgResId);
    }

    public CommonNoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.common_notice, this);
        image = (ImageView) findViewById(R.id.common_notice_img);
        text = (TextView) findViewById(R.id.common_notice_text);
    }

    public void setNotice(String notice) {
        text.setText(notice);
    }

    public void setImage(int resId) {
        image.setImageResource(resId);
    }

}
