package com.ye.player.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.ye.player.R;

public class ResizableImageView extends ImageView {

    private float ratio = -1;
    private int base = -1;
    public static final int WIDTH = 0;
    public static final int HEIGHT = 1;

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.ResizableImageView);
        ratio = t.getFloat(R.styleable.ResizableImageView_resize_ratio, -1);
        base = t.getInt(R.styleable.ResizableImageView_resize_base, -1);
        t.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (ratio > 0) {
            int height = View.MeasureSpec.getSize(heightMeasureSpec);
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            switch (base) {
                case WIDTH:
                    height = (int) Math.ceil((float) width * ratio);
                    break;
                case HEIGHT:
                    width = (int) Math.ceil((float) height * ratio);
                    break;
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}