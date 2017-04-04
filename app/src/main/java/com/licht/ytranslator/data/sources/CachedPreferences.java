package com.licht.ytranslator.data.sources;

import android.content.Context;
import android.content.SharedPreferences;

import com.licht.ytranslator.YTransApp;

import javax.inject.Inject;

/**
 * Хранит информацию о закэшированности данных для локализации
 */
public class CachedPreferences {
    private static final String PREF_NAME = "pref_name";

    private static final String PREF_DATA_CACHED_PREFIX = "DATA_CACHED_";

    private SharedPreferences mSharedPreferences;

    public CachedPreferences() {
        super();
        mSharedPreferences = (YTransApp.get()).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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
