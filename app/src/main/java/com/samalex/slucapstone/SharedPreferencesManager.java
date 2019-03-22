package com.samalex.slucapstone;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "com.example.app.PREF_NAME";
    private static final String KEY_VALUE = "com.example.app.KEY_VALUE";

    private static SharedPreferencesManager sInstance;
    private final SharedPreferences mPref;

    private SharedPreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferencesManager(context);
        }
    }

    public static synchronized SharedPreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(SharedPreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setValue(long value) {
        mPref.edit()
                .putLong(KEY_VALUE, value)
                .commit();
    }

    public long getValue() {
        return mPref.getLong(KEY_VALUE, 0);
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .commit();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }
}
