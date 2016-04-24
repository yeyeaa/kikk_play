package com.ye.player.common.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.ye.player.R;

public class ErrorViewUtil {
    private static View notificationView;

    public static void removeNotificationInView(ViewGroup parentView) {
        /*if (parentView.getId() == R.id.notice_container) {
            parentView.setVisibility(View.GONE);
        }*/
        /*View view = parentView.findViewById(R.id.common_notice_container);
        if (view != null) {
            parentView.removeView(view);
        }*/

        parentView.removeAllViews();
        parentView.setVisibility(View.GONE);
    }

    public static void clearErrorView() {
        notificationView = null;
    }

    public static void showNotificationInViewForEmpty(String text, ViewGroup parentView, final Runnable reloadRunnable) {
        removeNotificationInView(parentView);
        parentView.setVisibility(View.VISIBLE);
        Context context = parentView.getContext();
        View notificationView = LayoutInflater.from(context).inflate(R.layout.empty_notice, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        TextView textView = (TextView) notificationView.findViewById(R.id.empty_notice_text);
        if (!StringUtil.isEmpty(text)) {
            textView.setText(text);
        }
        parentView.addView(notificationView, layoutParams);
        if (reloadRunnable != null) {
            ImageView imageView = (ImageView) notificationView.findViewById(R.id.empty_notice_img);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reloadRunnable.run();
                }
            });
        }
    }

}
