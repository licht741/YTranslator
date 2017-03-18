package com.licht.ytranslator.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.licht.ytranslator.YTransApp;

public class AppPreferences {
    private static final String PREF_NAME = "pref_name";
    private static final String PREF_APP_LOCALIZATION = "PREF_APP_LOCAL";

    private static final String PREF_DATA_CACHED_PREFIX = "PREF_DATA_CACHED_";

    private SharedPreferences mSharedPreferences;

    public AppPreferences() {
        super();
        mSharedPreferences = YTransApp.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getAppLocalization() {
        return mSharedPreferences.getString(PREF_APP_LOCALIZATION, "");
    }

    public void putAppLocalization(String local) {
        mSharedPreferences.edit().putString(PREF_APP_LOCALIZATION, local).apply();
    }

    public Boolean getDataCached(String lang) {
        final String prefName = PREF_DATA_CACHED_PREFIX + lang;
        return mSharedPreferences.getBoolean(prefName, false);
    }

    public void putDataCached(String lang, Boolean value) {
        final String prefName = PREF_DATA_CACHED_PREFIX + lang;
        mSharedPreferences.edit().putBoolean(prefName, value).apply();
    }


}
