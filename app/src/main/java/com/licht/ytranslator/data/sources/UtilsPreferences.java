package com.licht.ytranslator.data.sources;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.FieldNamingPolicy;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.StringWrapper;

public class UtilsPreferences {
    private static final String PREF_NAME = "pref_ids";

    private static final String PREF_DICTIONARY_NUMBER = "DICT_NUMBER";

    private SharedPreferences mSharedPreferences;

    public UtilsPreferences() {
        super();
        mSharedPreferences = YTransApp.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public synchronized long generateDictionaryNumber() {
        final long crntNumber = getNumber(PREF_DICTIONARY_NUMBER);
        final long newValue = crntNumber + 1;
        setNumber(PREF_DICTIONARY_NUMBER, newValue);

        return newValue;
    }

    private long getNumber(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    private void setNumber(String key, long newValue) {
        mSharedPreferences.edit().putLong(key, newValue).apply();
    }
}
