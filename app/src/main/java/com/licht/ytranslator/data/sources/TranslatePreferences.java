package com.licht.ytranslator.data.sources;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.licht.ytranslator.YTransApp;

/**
 * Хранит информацию о последнем переводе (переводимый текст, направление перевода)
 */
public class TranslatePreferences {
    private static final String PREF_NAME = "user_preferences";

    private SharedPreferences mSharedPreferences;

    private static final String PREF_INPUT_TEXT = "INPUT_TEXT";
    private static final String PREF_TRANSLATE_DIRECTION = "TRANSLATE_DIRECTION";


    public TranslatePreferences() {
        super();
        mSharedPreferences = YTransApp.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    public String getInputText() {
        return mSharedPreferences.getString(PREF_INPUT_TEXT, null);
    }

    @Nullable
    public String getTranslateDirection() {
        return mSharedPreferences.getString(PREF_TRANSLATE_DIRECTION, null);
    }

    public void setInputText(String text) {
        mSharedPreferences.edit().putString(PREF_INPUT_TEXT, text).apply();
    }

    public void setDirectionText(String text) {
        mSharedPreferences.edit().putString(PREF_TRANSLATE_DIRECTION, text).apply();
    }

}
