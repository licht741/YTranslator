package com.licht.ytranslator.data.sources;

import android.content.Context;
import android.content.SharedPreferences;

import com.licht.ytranslator.YTransApp;

public class AppPreferences {
    private static final String PREF_NAME = "pref_name";

    private static final String PREF_DATA_CACHED_PREFIX = "DATA_CACHED_";
    private static final String PREF_SOURCE_LANG = "SOURCE_LANG";
    private static final String PREF_DESTINATION_LANG = "DESTINATION_LANG";

    private SharedPreferences mSharedPreferences;

    public AppPreferences() {
        super();
        mSharedPreferences = YTransApp.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public Boolean getDataCached(String lang) {
        final String prefName = PREF_DATA_CACHED_PREFIX + lang;
        return mSharedPreferences.getBoolean(prefName, false);
    }

    public void putDataCached(String lang, Boolean value) {
        final String prefName = PREF_DATA_CACHED_PREFIX + lang;
        mSharedPreferences.edit().putBoolean(prefName, value).apply();
    }

    public String getSourceLanguage() {
        return mSharedPreferences.getString(PREF_SOURCE_LANG, "");
    }

    public void putSourceLanguage(String lang) {
        mSharedPreferences.edit().putString(PREF_SOURCE_LANG, lang).apply();
    }


    public String getDestinationLanguage() {
        return mSharedPreferences.getString(PREF_DESTINATION_LANG, "");
    }

    public void putDestinationLanguage(String lang) {
        mSharedPreferences.edit().putString(PREF_DESTINATION_LANG, lang).apply();
    }

//    public String getTranslatingType() {
//        return mSharedPreferences.getString(PREF_TRANSLATING_TYPE, "");
//    }
//
//    public void setTranslatingType(String transType) {
//        mSharedPreferences.edit().putString(PREF_TRANSLATING_TYPE, transType).apply();
//
//    }

}
