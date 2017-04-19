package com.licht.ytranslator.data.sources;

import android.content.Context;
import android.content.SharedPreferences;

import com.licht.ytranslator.YTransApp;

/**
 * Realm не поддерживает автоинкрементальные идентификаторы, поэтому они реализуются вручную
 */
public class UtilsPreferences {
    private static final String PREF_NAME = "pref_ids";

    private static final String PREF_DICTIONARY_NUMBER = "DICT_NUMBER";

    private final SharedPreferences mSharedPreferences;

    public UtilsPreferences() {
        super();
        mSharedPreferences = YTransApp.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public synchronized long generateDictionaryNumber() {
        final long crntNumber = getNumber();
        final long newValue = crntNumber + 1;
        setNumber(newValue);

        return newValue;
    }

    private long getNumber() {
        return mSharedPreferences.getLong(UtilsPreferences.PREF_DICTIONARY_NUMBER, 0);
    }

    private void setNumber(long newValue) {
        mSharedPreferences.edit().putLong(UtilsPreferences.PREF_DICTIONARY_NUMBER, newValue).apply();
    }
}
