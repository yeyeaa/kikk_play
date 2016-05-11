package com.ye.player.common.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ye.player.R;
import com.ye.player.common.utils.ErrorViewUtil;
import com.ye.player.widget.CommonNoticeView;
import com.ye.player.widget.NavigationBar;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    private Dialog waitDialog;

    public NavigationBar navi;

    private OnBaseActivityCallbackListener callback;

    private boolean bDetroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinalTranslucentStatusBar()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (hasNavigationBar()) {
            navi = new NavigationBar(this);
            String title;
            try {
                title = getPackageManager().getActivityInfo(this.getComponentName(), 0).loadLabel(getPackageManager())
                        .toString();
                navi.setTitle(title);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

        if (callback != null) {
            callback.onCreate(savedInstanceState);
        }
    }

    public void setNavTitle(String title) {
        if (hasNavigationBar()) {
            navi.setTitle(title);
        }
    }

    public void setTitleImage(int resId) {
        if (hasNavigationBar()) {
            navi.setTitleImage(resId);
        }
    }

    public void setNavTitleView(View view) {
        if (hasNavigationBar()) {
            navi.setCustomTitleView(view);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (hasNavigationBar()) {
            ((FrameLayout) findViewById(android.R.id.content)).addView(navi, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            if (hasLeftBarButton()) {
                navi.setLeftButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }
    }

    protected int getModuleId() {
        return 0;
    }

    public void setCallback(OnBaseActivityCallbackListener callback) {
        this.callback = callback;
    }

    /**
     * show common notice with default text: network error
     */
    protected void showNotice() {
        CommonNoticeView notice = new CommonNoticeView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        ((FrameLayout) findViewById(android.R.id.content)).addView(notice, 1, lp);
    }

    /**
     * show common notice with special text
     */
    protected void showNotice(String text) {
        CommonNoticeView notice = new CommonNoticeView(this);
        notice.setNotice(text);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        ((FrameLayout) findViewById(android.R.id.content)).addView(notice, 1, lp);
    }

    @Override
    protected void onDestroy() {
        if (callback != null) {
            callback.onDestroy();
        }
        if (waitDialog != null && waitDialog.isShowing()) {
            try {
                waitDialog.cancel();
            } catch (Exception e) {
            }
            waitDialog = null;
        }
        ErrorViewUtil.clearErrorView();
        super.onDestroy();
        bDetroyed = true;
    }

    public boolean isActivityDestroyed() {
        return bDetroyed;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }


    private boolean createWaitDialog(String message) {
        if (isActivityDestroyed()) {
            return false;
        }
        if (waitDialog != null && waitDialog.isShowing()) {
            ((TextView) waitDialog.findViewById(R.id.message)).setText(message);
            return true;
        }

        waitDialog = new Dialog(this, R.style.dialogProgress);
        waitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        waitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        waitDialog.setContentView(R.layout.dialog_progress);
        ((TextView) waitDialog.findViewById(R.id.message)).setText(message);
        return true;
    }



    public void showWaitDialog(String message) {
        if (createWaitDialog(message)) {
            waitDialog.setCancelable(false);
            waitDialog.show();
        }
    }


    public void dismissWaitDialog() {
        if (!this.isActivityDestroyed() && waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
        waitDialog = null;
    }

    public void setRightButton(String str, View.OnClickListener listener) {
        if (hasNavigationBar()) {
            navi.setRightText(str, listener);
        }
    }

    public void setRightTextVisible(boolean visible) {
        navi.setRightTextVisible(visible);
    }

    public void setRightTextColor(boolean visible) {
        navi.setRightTextBackground(visible);
    }

    public static interface OnBaseActivityCallbackListener {

        public void onCreate(Bundle savedInstanceState);

        public void onDestroy();
    }

    public boolean hasToolBar() {
        return false;
    }

    public boolean hasNavigationBar() {
        return true;
    }

    public boolean hasLeftBarButton() {
        return true;
    }

    public boolean isTranslucentStatusBar() {
        return false;
    }

    protected final boolean isFinalTranslucentStatusBar() {
        return isTranslucentStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public void noAnimationFinish() {
        finish();
        overridePendingTransition(0, R.anim.anim_exit);
    }

    public void setNaviAlpha(float alpha){
        if (hasNavigationBar()) {
            navi.setAlpha(alpha);
        }
    }
}
