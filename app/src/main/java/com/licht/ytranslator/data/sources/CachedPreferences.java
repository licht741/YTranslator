package com.licht.ytranslator.data.sources;

import android.content.Context;
import android.content.SharedPreferences;

import com.licht.ytranslator.YTransApp;

/**
 * Хранит информацию о закэшированности данных для локализации
 */
public class CachedPreferences {
    private static final String PREF_NAME = "pref_name";

    private static final String PREF_DATA_CACHED_PREFIX = "DATA_CACHED_";

    private final SharedPreferences mSharedPreferences;

    public CachedPreferences() {
        super();
        mSharedPreferences = (YTransApp.get()).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Проверяет наличие загруженной локализации для указанного языка
     * @param lang Символ языка
     * @return True, если локализация была загружена, иначе False
     */
    public Boolean getDataCached(String lang) {
        final String prefName = PREF_DATA_CACHED_PREFIX + lang;
        return mSharedPreferences.getBoolean(prefName, false);
    }

    /**
     * Помечает, что локализация для указанного языка была загружена
     * @param lang Символ языка
     */
    public void putDataCached(String lang) {
        final String prefName = PREF_DATA_CACHED_PREFIX + lang;
        mSharedPreferences.edit().putBoolean(prefName, true).apply();
    }

}
