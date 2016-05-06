package com.ye.player.common.ui;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.ye.player.common.utils.ErrorViewUtil;
import com.ye.player.widget.NavigationBar;

/**
 * Created by yexiaoyou on 2016/5/6.
 */
public class BaseFragment extends Fragment {

    public NavigationBar navi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinalTranslucentStatusBar()) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (hasNavigationBar()) {
            navi = new NavigationBar(getActivity());
            String title;
            try {
                title = getActivity().getPackageManager().getActivityInfo(getActivity().getComponentName(), 0)
                        .loadLabel(getActivity().getPackageManager()).toString();
                navi.setTitle(title);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    public void setNavTitle(String title) {
        if (hasNavigationBar()) {
            navi.setTitle(title);
        }
    }

    @Override
    public void onDestroy() {
        ErrorViewUtil.clearErrorView();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasNavigationBar()) {

            if (getView() instanceof ScrollView) {
                ((ViewGroup) ((ViewGroup) getView()).getChildAt(0)).removeView(navi);
                ((ViewGroup) ((ViewGroup) getView()).getChildAt(0)).addView(navi, 0, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            } else {
                ((ViewGroup) getView()).removeView(navi);
                ((ViewGroup) getView()).addView(navi, 0, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }


    public void setRightButton(String str, View.OnClickListener listener) {
        if (hasNavigationBar()) {
            navi.setRightText(str, listener);
        }
    }

    public void showWaitDialog(String message) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((BaseFragmentActivity) getActivity()).showWaitDialog(message);
        }
    }

    public void dismissWaitDialog() {
        if (getActivity() != null) {
            ((BaseFragmentActivity) getActivity()).dismissWaitDialog();
        }
    }

    public boolean hasNavigationBar() {
        return false;
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

    public boolean hasNavigatBackground() {
        return false;
    }
}
