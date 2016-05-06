package com.ye.player.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.ye.player.KikkApplication;
import com.ye.player.R;
import com.ye.player.common.ui.activity.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int[] getDeviceSize(Context context) {
		int[] size = new int[2];

		int measuredWidth = 0;
		int measuredHeight = 0;
		Point point = new Point();
		WindowManager wm = ((WindowManager) KikkApplication.app.getSystemService(Context.WINDOW_SERVICE));

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			wm.getDefaultDisplay().getSize(point);
			measuredWidth = point.x;
			measuredHeight = point.y;
		} else {
			DisplayMetrics dm = KikkApplication.app.getResources().getDisplayMetrics();
			measuredWidth = dm.widthPixels;
			measuredHeight = dm.heightPixels;
		}

		size[0] = measuredWidth;
		size[1] = measuredHeight;
		return size;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		return w_screen;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int h_screen = dm.heightPixels;
		return h_screen;
	}

	public static int getDeviceWidth(Context context) {
		return getDeviceSize(context)[0];
	}

	public static int getDeviceHeight(Context context) {
		return getDeviceSize(context)[1];
	}

	public static float getDensity(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
		wm.getDefaultDisplay().getMetrics(metrics);

		return metrics.density;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static boolean isPackageExisted(Context context, String packageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
		}
		return packageInfo != null;
	}

	public static void openApp(Context context, String packageName) throws NameNotFoundException {
		PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);

		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

		Iterator<ResolveInfo> iterator = apps.iterator();
		while (iterator.hasNext()) {
			ResolveInfo ri = iterator.next();
			if (ri != null) {
				packageName = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				ComponentName cn = new ComponentName(packageName, className);
				intent.setComponent(cn);
				context.startActivity(intent);
			}
		}
	}

	public static List<PackageInfo> getNonSystemApps(Context context) {
		List<PackageInfo> list = context.getPackageManager().getInstalledPackages(0);
		List<PackageInfo> systemApps = new ArrayList<PackageInfo>();
		for (PackageInfo info : list) {
			if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				systemApps.add(info);
			}
		}
		list.removeAll(systemApps);
		return list;
	}

	public static void showToast(Context context, int id) {
		Toast.makeText(KikkApplication.app, KikkApplication.app.getResources().getString(id), Toast.LENGTH_SHORT)
				.show();
	}

	public static void showToast(Context context, String string) {
		if (!StringUtil.isEmpty(string)) {
			Toast.makeText(KikkApplication.app, string, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showToastLong(Context context, int id) {
		Toast.makeText(KikkApplication.app, KikkApplication.app.getResources().getString(id), Toast.LENGTH_LONG)
				.show();
	}

	public static void showToastLong(Context context, String string) {
		if (!StringUtil.isEmpty(string)) {
			Toast.makeText(KikkApplication.app, string, Toast.LENGTH_LONG).show();
		}
	}

	public static void showToast(int id) {
		Toast.makeText(KikkApplication.app, KikkApplication.app.getResources().getString(id), Toast.LENGTH_SHORT)
				.show();
	}

	public static void showToast(String string) {
		if (!StringUtil.isEmpty(string)) {
			Toast.makeText(KikkApplication.app, string, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showToastLong(int id) {
		Toast.makeText(KikkApplication.app, KikkApplication.app.getResources().getString(id), Toast.LENGTH_LONG)
				.show();
	}

	public static void showToastLong(String string) {
		if (!StringUtil.isEmpty(string)) {
			Toast.makeText(KikkApplication.app, string, Toast.LENGTH_LONG).show();
		}
	}

	public static int getVersionCode() {
		int versionCode = Integer.MAX_VALUE;
		try {
			versionCode = KikkApplication.app.getPackageManager().getPackageInfo(
					KikkApplication.app.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public static String getVersionName(Context context) {
		try {
			String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			return versionName;
		} catch (Exception e) {
		}
		return "Unknown";
	}

	public static Map<String, Object> buildMap(Object... keyValues) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (int i = 0; i < keyValues.length; i += 2) {
			resultMap.put((String) keyValues[i], keyValues[i + 1]);
		}
		return resultMap;
	}

	/*public static String getMacAddress() {
		WifiManager wimanager = (WifiManager) KikkApplication.app.getSystemService(Context.WIFI_SERVICE);

		String macAddress = null;
		try {
			macAddress = wimanager.getConnectionInfo().getMacAddress();
		} catch (Exception e) {
		}

		if (StringUtil.isEmpty(macAddress)) {
			macAddress = "000000000000";
		} else {
			macAddress = macAddress.replace(":", "");
		}

		return macAddress;
	}*/

	public static String getAndroidId() {
		String androidId = null;
		try {
			Context context = KikkApplication.app;
			ContentResolver contentResolver = context.getContentResolver();
			androidId = Secure.getString(contentResolver, Secure.ANDROID_ID);
		} catch (Exception e) {
		}
		if (StringUtil.isEmpty(androidId)) {
			androidId = "0";
		}

		return androidId;
	}

	public static String convertByte(long total) {
		if (total < 1024) {
			return total + " B";
		} else if (total < 1024 * 1024) {
			return total / 1024 + " KB";
		} else {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			return nf.format(total / 1024 / 1024.0) + " MB";
		}
	}

	public static String convertSpeed(long total) {
		if (total < 1024) {
			return total + " B/s";
		} else if (total < 1024 * 1024) {
			return total / 1024 + " KB/s";
		} else {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			return nf.format(total / 1024 / 1024.0) + " MB/s";
		}
	}

	public static String convertByte(Long total) {
		if (total == null) {
			return "0 MB";
		}
		return convertByte(total.longValue());
	}

	public static String convertByteToKiloByte(long total) {
		return total / 1024 + "KB";
	}

	public static String getAppChannel(Context context) {
		String appChannel = "";
		try {
			Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA).metaData;
			appChannel = (String) metaData.get("UMENG_CHANNEL");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return appChannel != null ? appChannel : "";
	}

	public static void startApkInstall(Context context, String path) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(i);
		} catch (Exception re) {
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("deprecation")
	public static void copyToClipboard(Context context, String content) {
		try {
			if (Build.VERSION.SDK_INT > 11) {
				android.content.ClipboardManager cmb = (android.content.ClipboardManager) context
						.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setPrimaryClip(ClipData.newPlainText(null, content));
			} else {
				android.text.ClipboardManager cmb = (android.text.ClipboardManager) context
						.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(content);
			}
		} catch (Exception e) {
		}
	}

	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");
		String regEx = "[『』]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/*public static void showNotificationDialog(Activity activity, String title, String content) {
		if (activity != null && activity instanceof BaseActivity) {
			if (((BaseActivity) activity).isActivityDestroyed()) {
				return;
			}
		}
		final CustomDialog dialog = new CustomDialog(activity);
		dialog.show();
		dialog.setCustomTitle(title);
		dialog.setCustomMessage(content);
		dialog.setBackgroundResource(R.drawable.button_primary_red);
		dialog.setConfirmButtonOnClickListener(R.string.common_string_confirm, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
	}*/


	public static long calculateDiskCacheSize(File paramFile) {
		long l1 = 20000000L;
		try {
			StatFs statFs = new StatFs(paramFile.getAbsolutePath());
			long l2 = statFs.getBlockCount() * statFs.getBlockSize() / 50L;
			l1 = l2;
		} catch (IllegalArgumentException e) {
		}
		return Math.max(Math.min(l1, 200000000L), 20000000L);
	}

	public static void setViewVisibility(View view, int visibility) {
		if (view != null && view.getVisibility() != visibility) {
			view.setVisibility(visibility);
		}
	}

	public static boolean isVersionEqualOrNewerThan(int version) {
		return Build.VERSION.SDK_INT >= version;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void setBackground(View view, Drawable drawable) {
		if (Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}

	/*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static Drawable getDrawable(Resources resources, int id){
		if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
			return resources.getDrawable(R.drawable.market_search_tagview,null);
		} else {
			return resources.getDrawable(id);
		}

	}*/
}
