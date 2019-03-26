package com.test.sixpro.retrofit.util;

import android.content.Context;
import android.content.SharedPreferences;


public class ShareUtil {

    private String path = "Retrofit_SP";
    private static ShareUtil prefUtils;
    private final SharedPreferences sp;

    public ShareUtil(Context context) {
        sp = context.getSharedPreferences(path, Context.MODE_PRIVATE);
    }

    public static ShareUtil create(Context context) {
        if (prefUtils == null) {
            synchronized (ShareUtil.class) {
                if (prefUtils == null) {
                    prefUtils = new ShareUtil(context);
                }
            }
        }
        return prefUtils;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void putBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public void putString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public void putInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public void putLong(String key, Long value) {
        sp.edit().putLong(key, value).apply();
    }

    public Long getLong(String key, Long defValue) {
        return sp.getLong(key, defValue);
    }

    public void remove(String key) {
        sp.edit().remove(key).apply();
    }

    public void clear() {
        if (sp != null)
            sp.edit().clear().apply();
    }
}
