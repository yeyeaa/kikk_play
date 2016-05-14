package com.ye.player.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.ye.player.KikkApplication;

/**
 * Created by yexiaoyou on 2016/5/14.
 */
public class KikkPreferenceManager {

    private static KikkPreferenceManager instance;
    private SharedPreferences preference;

    private KikkPreferenceManager(Context context) {
        preference = context.getSharedPreferences(Constants.PREPERENCE_KEY,Context.MODE_PRIVATE);
    }

    public static KikkPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new KikkPreferenceManager(context);
        }
        return instance;
    }

    public static KikkPreferenceManager getInstance() {
        if (instance == null) {
            Context context = KikkApplication.app.getApplicationContext();
            instance = new KikkPreferenceManager(context);
        }
        return instance;
    }

    public void savePreferencesString(String key, String value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreferencesString(String key) {
        return preference.getString(key, null);
    }

    public void savePreferencesBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getPreferencesBoolean(String key, boolean defValue) {
        return preference.getBoolean(key, defValue);
    }

    public void savePreferencesInteger(String key, int value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getPreferencesInteger(String key, int defValue) {
        return preference.getInt(key, defValue);
    }

    public long getPreferencesLong(String key, int defValue) {
        return preference.getLong(key, 0);
    }

    public void savePreferencesLong(String key, long value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void persistRelatedTask(String name, String desc, String url, String thumbUrl) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("download_name", name);
        editor.putString("download_desc", desc);
        editor.putString("download_url", url);
        editor.putString("download_thumburl", thumbUrl);
        editor.commit();
    }

    public void deleteRelatedTask() {
        SharedPreferences.Editor editor = preference.edit();
        editor.remove("download_name");
        editor.remove("download_desc");
        editor.remove("download_url");
        editor.remove("download_thumburl");
        editor.commit();
    }
}