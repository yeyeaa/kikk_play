package com.ye.player.ad.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings.PluginState;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.tauth.UiError;

import com.ye.player.R;
import com.ye.player.common.ui.activity.BaseActivity;
import com.ye.player.common.utils.ErrorViewUtil;
import com.ye.player.common.utils.StringUtil;
import com.ye.player.common.utils.URIUtils;
import com.ye.player.common.utils.Utils;
import com.ye.player.widget.BrowserBottomNavigationBar;
import com.ye.player.widget.GradualPopupWindow;


public class CustomBrowserActivity extends BaseActivity implements OnClickListener, OnTouchListener, DownloadListener {

	public static final String REFRESH_BROWSER = "refresh-browser";
	private ProgressBar progressBar;
	protected WebView webView;
	public String targetUrl = null;
	private String currentUrl;
	private LinearLayout parent;
	private LinearLayout ll;
	private GradualPopupWindow gradualPopupWindow = null;
	private RelativeLayout noticeContainer;
	private TextView forward;
	private TextView backward;
	private boolean loadedError = false;
	private boolean hideNavigationBar;
	private boolean openWeb;
	private BrowserBottomNavigationBar browserBottomNavigationBar = null;
	//private ShareDialog dialog = null;
	private boolean needRefresh = false;
	private String refreshUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_custom_browser);
		init();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		initViews();
	}

	@SuppressLint("SetJavaScriptEnabled")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void init() {
		noticeContainer = (RelativeLayout) findViewById(R.id.notice_container);
		progressBar = (ProgressBar) findViewById(R.id.custom_browser_progressbar);
		parent = (LinearLayout) findViewById(R.id.webview_parent);
		ll = (LinearLayout) findViewById(R.id.webview_container);
		webView = new WebView(this);
		WebView.LayoutParams params = new WebView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		webView.setLayoutParams(params);
		ll.addView(webView);
		/*if (!webView.getSettings().getUserAgentString().contains(NetworkUtils.AGENT_TAG)) {
			webView.getSettings().setUserAgentString(
					webView.getSettings().getUserAgentString() + NetworkUtils.AGENT_TAG);
		}*/
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
		webView.getSettings().setAppCachePath(appCachePath);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setUseWideViewPort(getIntent().getBooleanExtra("isSupportZoom", true));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.getSettings().setDisplayZoomControls(false);
			webView.getSettings().setSupportZoom(getIntent().getBooleanExtra("isSupportZoom", true));
			webView.getSettings().setBuiltInZoomControls(true);
		}

		webView.setWebChromeClient(new CustomWebChromeClient());
		webView.setWebViewClient(new CustomWebViewClient());
		webView.setOnTouchListener(this);
		webView.setOnClickListener(this);
		webView.setDownloadListener(this);
		//webView.addJavascriptInterface(getHtmlObject(), "hsputil");
		//ObserverCenter.register(REFRESH_BROWSER, refreshObserver);
	}

	private void initViews() {
		initRightTopMenu();
		if (getIntent().getBooleanExtra("isBackStyle", false)) {
			navi.setLeftButton(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (webView != null && webView.canGoBack()) {
						webView.goBack();
					} else {
						finish();
					}
				}
			});
		} else {
			navi.setLeftButton(R.drawable.navi_bar_exit_img);
		}
		hideNavigationBar = getIntent().getBooleanExtra("hideNavigationBar", false);
		openWeb = getIntent().getBooleanExtra("openWeb", false);
		if (hideNavigationBar) {
			navi.setVisibility(View.INVISIBLE);
			browserBottomNavigationBar = (BrowserBottomNavigationBar) findViewById(R.id.bottom_navi);
			browserBottomNavigationBar.setOnClickListener(this);
			browserBottomNavigationBar.setVisibility(View.VISIBLE);
		}

		if (getIntent().getBooleanExtra("hideBottomNavigationBar", false) && browserBottomNavigationBar != null) {
			browserBottomNavigationBar.setVisibility(View.GONE);
		}
		initGradualPopupWindow(hideNavigationBar);
	}

	/*private Object getHtmlObject() {
		Object htmlObject = new Object() {
			@JavascriptInterface
			public void Share(final String title, final String desc, final String url) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showShareDialog(url, title, desc);
					}
				});
			}

			@JavascriptInterface
			public String getUserInfo() {
				return JSONUtil.toJSONString(new UserInfoService(CustomBrowserActivity.this).getCurrentUserInfo());
			}

			@JavascriptInterface
			public void saveItemAction(int itemType, int actionType, long itemId, String extra) {
				DataCollectUtils.saveItemAction(itemType, actionType, itemId, extra);
			}
		};
		return htmlObject;
	}*/

	private void initGradualPopupWindow(boolean hideNavigationBar) {
		if (gradualPopupWindow == null) {
			gradualPopupWindow = new GradualPopupWindow(this);
			View contentView = null;
			if (hideNavigationBar) {
				contentView = gradualPopupWindow.setContentView(R.layout.browser_share_popup_window);
				if (openWeb) {
					contentView.findViewById(R.id.share_to_wechat_moments).setVisibility(View.VISIBLE);
					contentView.findViewById(R.id.share_to_wechat_moments).setOnClickListener(this);
				}
				contentView.findViewById(R.id.share_to_wechat_moments).setOnClickListener(this);
				contentView.findViewById(R.id.share_to_wechat).setOnClickListener(this);
				contentView.findViewById(R.id.share_to_qq_zone).setOnClickListener(this);
			} else {
				contentView = gradualPopupWindow.setContentView(R.layout.browser_more_popup_window);
				contentView.findViewById(R.id.share_to_wechat_moments).setOnClickListener(this);
				contentView.findViewById(R.id.share_to_wechat).setOnClickListener(this);
				contentView.findViewById(R.id.share_to_qq_zone).setOnClickListener(this);
				contentView.findViewById(R.id.open_in_browser_outside).setOnClickListener(this);
				contentView.findViewById(R.id.browser_refresh).setOnClickListener(this);
				backward = (TextView) contentView.findViewById(R.id.browser_backward);
				if (webView.canGoBack()) {
					backward.setCompoundDrawablesWithIntrinsicBounds(null,
							ContextCompat.getDrawable(this, R.drawable.browser_icon_backward_enable), null, null);
				} else {
					backward.setCompoundDrawablesWithIntrinsicBounds(null,
							ContextCompat.getDrawable(this, R.drawable.browser_icon_backward_disable), null, null);
				}
				backward.setOnClickListener(this);

				forward = (TextView) contentView.findViewById(R.id.browser_forward);
				if (webView.canGoForward()) {
					forward.setCompoundDrawablesWithIntrinsicBounds(null,
							ContextCompat.getDrawable(this, R.drawable.browser_icon_forward_enable), null, null);
				} else {
					forward.setCompoundDrawablesWithIntrinsicBounds(null,
							ContextCompat.getDrawable(this, R.drawable.browser_icon_forward_disable), null, null);
				}
				forward.setOnClickListener(this);
			}
		}
	}

	private void showBottomShareWindow(View view) {
		gradualPopupWindow.show(view, RelativeLayout.ALIGN_PARENT_BOTTOM, 0, 0);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		targetUrl = getIntent().getStringExtra("url");
		if (!StringUtil.isEmpty(targetUrl) && !targetUrl.equals(currentUrl)) {
			currentUrl = targetUrl;
			progressBar.setVisibility(View.VISIBLE);
			webView.loadUrl(URIUtils.appendHttpPrefix(currentUrl));
		}
		String tag = getIntent().getStringExtra("title");
		if (!StringUtil.isEmpty(tag)) {
			setNavTitle(tag);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.onResume();
		}
		webView.resumeTimers();
		int paddingTop = parent.getPaddingTop();
		boolean hideNavigationBar = getIntent().getBooleanExtra("hideNavigationBar", false);
		if (hideNavigationBar && paddingTop > 0) {
			navi.setVisibility(View.GONE);
			paddingTop = 0;
			parent.setPadding(0, paddingTop, 0, 0);
		} else if (!hideNavigationBar && paddingTop == 0) {
			navi.setVisibility(View.VISIBLE);
			paddingTop = (int) getResources().getDimension(R.dimen.navi_bar_height);
			parent.setPadding(0, paddingTop, 0, 0);
		}
		if (!hideNavigationBar) {
			if (!getIntent().getBooleanExtra("hideTopMenu", false)) {
				navi.setRightButtonVisible(true);
			} else {
				navi.setRightButtonVisible(false);
			}
		}

		if (needRefresh) {
			if (!TextUtils.isEmpty(refreshUrl)) {
				webView.loadUrl(URIUtils.appendHttpPrefix(refreshUrl));
				refreshUrl = null;
			}
			needRefresh = false;
		}
	}

	@Override
	protected void onStop() {
		if (gradualPopupWindow != null) {
			gradualPopupWindow.dismiss();
		}
		super.onStop();
	}

	@Override
	protected void onPause() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			webView.onPause();
		}
		webView.pauseTimers();
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		if (webView != null) {
			try {
				((ViewGroup) webView.getParent()).removeView(webView);
				webView.getSettings().setBuiltInZoomControls(true);
				webView.removeAllViews();
				webView.clearCache(false);
				webView.freeMemory();
				webView.destroy();
			} catch (Exception e) {
			}
			webView = null;
		}

	/*	if (dialog != null) {
			dialog.dismiss();
		}*/

		/*ObserverCenter.unregister(REFRESH_BROWSER, refreshObserver);*/
		super.onDestroy();
		Thread.interrupted();
	}

	@Override
	public void onClick(View v) {
		String title = navi == null ? getString(R.string.me_app_direction) : "" + navi.title.getText();
		String desc = navi == null ? getString(R.string.me_app_desc) : "" + navi.title.getText();
		if (gradualPopupWindow != null) {
			gradualPopupWindow.dismiss();
		}
		switch (v.getId()) {
			case R.id.browser_forward:
				if (webView.canGoForward()) {
					webView.goForward();
				}
				break;
			case R.id.backward_btn:
				if (webView.canGoBack()) {
					webView.goBack();
				} else {
					finish();
				}
				break;
			case R.id.browser_backward:
				if (webView.canGoBack()) {
					webView.goBack();
				}
				break;
			case R.id.refresh_btn:
			case R.id.browser_refresh:
				ErrorViewUtil.removeNotificationInView(noticeContainer);
				loadedError = false;
				webView.reload();
				progressBar.setVisibility(View.VISIBLE);
				break;
			case R.id.open_in_browser_outside:
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl));
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					startActivity(intent);
				} catch (RuntimeException re) {
					re.printStackTrace();
				}
				break;

			case R.id.share_to_wechat:
				Utils.showToast(CustomBrowserActivity.this, "敬请期待");
				break;
			case R.id.share_to_wechat_moments:
				Utils.showToast(CustomBrowserActivity.this, "敬请期待");
				break;
			case R.id.share_to_qq_zone:
				Utils.showToast(CustomBrowserActivity.this, "敬请期待");
				break;
			/*case R.id.share_to_wechat:
				ShareUtils.shareLinkToWechatSession(CustomBrowserActivity.this,
						String.valueOf(System.currentTimeMillis()), title, desc,
						BitmapUtil.loadBitmap(CustomBrowserActivity.this, R.drawable.sharelogo), webView.getUrl());
				break;
			case R.id.share_to_wechat_moments:
				ShareUtils.shareLinkToWechatTimeline(CustomBrowserActivity.this,
						String.valueOf(System.currentTimeMillis()), title, desc,
						BitmapUtil.loadBitmap(CustomBrowserActivity.this, R.drawable.sharelogo), webView.getUrl());
				break;
			case R.id.share_to_qq_zone:
				ShareUtils.shareToQzone(CustomBrowserActivity.this, title, desc, webView.getUrl(),
						UrlConstants.URL_QZONE_SHARE, new TencentBaseUiListener() {
							@Override
							public void onError(UiError e) {
								Utils.showToastLong(getApplicationContext(), "分享到Qzone出错了");
							}
						});
				break;*/
			case R.id.close_btn:
				finish();
				break;
			/*case R.id.share_btn:
				showBottomShareWindow(parent);
				break;*/

			default:
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
			case R.id.webview_container:
				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						v.onTouchEvent(event);
						return true;
				}
				break;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class CustomWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			if (loadedError) {
				view.setVisibility(View.INVISIBLE);
				return;
			}
			view.setVisibility(View.VISIBLE);
			onLoadFinished();
		}

		@Override
		public void onReceivedError(final WebView view, int errorCode, String description, String failingUrl) {
			loadedError = true;
			view.setVisibility(View.INVISIBLE);
			ErrorViewUtil.showNotificationInView(getString(R.string.network_error), noticeContainer, new Runnable() {
				@Override
				public void run() {
					loadedError = false;
					view.reload();
				}
			}, true);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("http") || url.startsWith("https")) {
				return super.shouldOverrideUrlLoading(view, url);
			} else {
				if (!needRefresh) {
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (url.startsWith("hsp")) {
						needRefresh = true;
					}
				}
			}
			return true;
		}
	}

	private class CustomWebChromeClient extends WebChromeClient {

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback callback) {
			super.onGeolocationPermissionsShowPrompt(origin, callback);
			callback.invoke(origin, true, false);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			progressBar.setProgress(Math.abs(newProgress));
			if (newProgress == 100) {
				progressBar.setVisibility(View.GONE);
			} else if (newProgress == 0) {
				progressBar.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			super.onReceivedIcon(view, icon);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			String tag = getIntent().getStringExtra("title");
			if (StringUtil.isEmpty(tag)) {
				setNavTitle(title);
			}
			super.onReceivedTitle(view, title);
		}
	}

	@Override
	public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
			long contentLength) {
		Utils.startActivityForBrowser(this, url);
	}

	public boolean isLoadedError() {
		return loadedError;
	}

	private void initRightTopMenu() {
		navi.setRightButton(R.drawable.but_more, new OnClickListener() {

			@Override
			public void onClick(View v) {
				refreshNavigationBtnState();
				showBottomShareWindow(parent);
			}
		});
	}

	private void refreshNavigationBtnState() {
		if (webView == null) {
			return;
		}
		if (backward != null) {
			if (webView.canGoBack()) {
				backward.setEnabled(true);
				backward.setCompoundDrawablesWithIntrinsicBounds(null,
						ContextCompat.getDrawable(this, R.drawable.browser_icon_backward_enable), null, null);
			} else {
				backward.setEnabled(false);
				backward.setCompoundDrawablesWithIntrinsicBounds(null,
						ContextCompat.getDrawable(this, R.drawable.browser_icon_backward_disable), null, null);
			}
		}
		if (forward != null) {
			if (webView.canGoForward()) {
				forward.setEnabled(true);
				forward.setCompoundDrawablesWithIntrinsicBounds(null,
						ContextCompat.getDrawable(this, R.drawable.browser_icon_forward_enable), null, null);
			} else {
				forward.setEnabled(false);
				forward.setCompoundDrawablesWithIntrinsicBounds(null,
						ContextCompat.getDrawable(this, R.drawable.browser_icon_forward_disable), null, null);
			}
		}
	}

	protected void onLoadFinished() {
		refreshNavigationBtnState();
	}

/*	public void showShareDialog(String shareUrl, String title, String desc) {
		if (isFinishing()) {
			return;
		}
		dialog = new ShareDialog(CustomBrowserActivity.this, false, shareUrl, title, desc, false);
		dialog.show();
		dialog.setQrinvisible(false);
	}*/

	/*private Observer refreshObserver = new Observer() {
		@Override
		public void notify(Object data) {
			refreshUrl = (String) data;
		}
	};*/
}
