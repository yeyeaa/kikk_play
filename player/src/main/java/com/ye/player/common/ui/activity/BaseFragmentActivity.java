package com.ye.player.common.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ye.player.R;
import com.ye.player.common.utils.ErrorViewUtil;
import com.ye.player.widget.NavigationBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yexiaoyou on 2016/5/6.
 */
public class BaseFragmentActivity extends FragmentActivity {

    private Dialog waitDialog;

    protected NavigationBar navi;

    private boolean bDetroyed = false;

    protected boolean mStateSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (hasNavigationBar()) {
            ((FrameLayout) findViewById(android.R.id.content)).addView(navi, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStateSaved = false;
    }

    protected int getModuleId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
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

    public void setRightButton(int resId) {
        if (hasNavigationBar()) {
            navi.setRightButton(resId);
        }
    }

    public void setRightButton(int resId, View.OnClickListener listener) {
        if (hasNavigationBar()) {
            navi.setRightButton(resId, listener);
        }
    }

    public void setRightText(View.OnClickListener listener) {
        if (hasNavigationBar()) {
            navi.setRightText(listener);
        }
    }

    public void setRightTextVisible(boolean visible) {
        navi.setRightTextVisible(visible);
    }

    public void setRightText(CharSequence str) {
        navi.setRightText(str);
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

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStateSaved = true;
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (Exception e) {
            try {
                this.finish();
            } catch (Exception e1) {
            }
        }
    }

    public boolean isActivityDestroyed() {
        return bDetroyed;
    }

    public boolean isSaveInstanceState() {
        return mStateSaved;
    }

    private boolean createWaitDialog(String message) {
        if (isActivityDestroyed()) {
            return false;
        }
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
            waitDialog = null;
        }

        waitDialog = new Dialog(this, R.style.dialogProgress);
        waitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        waitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        waitDialog.setContentView(R.layout.dialog_progress);
        ((TextView) waitDialog.findViewById(R.id.message)).setText(message);
        return true;
    }

    public void showWaitDialog(String message, boolean isCancelable, DialogInterface.OnCancelListener listener) {
        if (createWaitDialog(message)) {
            waitDialog.setCanceledOnTouchOutside(false);
            waitDialog.setCancelable(isCancelable);
            waitDialog.setOnCancelListener(listener);
            waitDialog.show();
        }
    }

    public void showWaitDialog(int stringId, boolean isCancelable, DialogInterface.OnCancelListener listener) {
        if (createWaitDialog(getResources().getString(stringId))) {
            waitDialog.setCanceledOnTouchOutside(false);
            waitDialog.setCancelable(isCancelable);
            waitDialog.setOnCancelListener(listener);
            waitDialog.show();
        }
    }

    public void showWaitDialog(String message) {
        if (createWaitDialog(message)) {
            waitDialog.setCancelable(true);
            waitDialog.show();
        }
    }

    public void showWaitDialog(int stringId) {
        if (createWaitDialog(getResources().getString(stringId))) {
            waitDialog.setCancelable(true);
            waitDialog.show();
        }
    }


    public void setNavBackground(int resId) {
        if (hasNavigationBarBackground()) {
            navi.setBackgroundLayout(resId);
        }
    }

    public void setNavTitleColor(int resId) {
        if (hasNavigationBarBackground()) {
            navi.setTitleColor(resId);
        }
    }

    public void dismissWaitDialog() {
        if (!this.isActivityDestroyed() && waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
        waitDialog = null;
    }

    public void noAnimationFinish() {
        finish();
        overridePendingTransition(0, R.anim.anim_exit);
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

    public boolean hasNavigationBarBackground() {
        return false;
    }

    public boolean isTranslucentStatusBar() {
        return false;
    }

    protected final boolean isFinalTranslucentStatusBar() {
        return isTranslucentStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
