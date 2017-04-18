package com.licht.ytranslator.data.sources;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.licht.ytranslator.YTransApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Хранит информацию о последнем переводе (переводимый текст, направление перевода)
 */
public class TranslatePreferences {
    private static final String PREF_NAME = "user_preferences";

    private final SharedPreferences mSharedPreferences;

    private static final String PREF_INPUT_TEXT = "INPUT_TEXT";
    private static final String PREF_TRANSLATE_DIRECTION = "TRANSLATE_DIRECTION";
    private static final String PREF_RECENTLY_USED_LANGUAGES = "RECENTLY_USED_LANGUAGES";


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

    public ArrayList<String> getRecentlyUsedLanguages() {
        String languages = mSharedPreferences.getString(PREF_RECENTLY_USED_LANGUAGES, null);
        if (languages == null) {
            setRecentlyUsedLanguages(new ArrayList<>());
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(languages.split(";")));
    }

    public void setRecentlyUsedLanguages(List<String> languages) {
        final StringBuilder buffer = new StringBuilder();

        for (String lang : languages)
            buffer.append(lang).append(";");
        if (languages.size() > 0)
            buffer.deleteCharAt(buffer.length() - 1);

        mSharedPreferences.edit().putString(PREF_RECENTLY_USED_LANGUAGES, buffer.toString()).apply();
    }

    private final int MAX_LANGUAGES_IN_HISTORY = 5;

    public void updateRecentlyUsedLanguage(String language) {
        final List<String> recentlyUsedLanguages = getRecentlyUsedLanguages();

        // Обрабатываем исключительную ситуацию, когда у нас записана пустая строка
        // (возможна, если мы пока что не добавляли языков в список недавних)
        if (recentlyUsedLanguages.size() == 1 &&
                recentlyUsedLanguages.get(0).equals("")) {
            setRecentlyUsedLanguages(Collections.singletonList(language));
            return;
        }


        if (recentlyUsedLanguages.contains(language))
            return;


        if (recentlyUsedLanguages.size() < MAX_LANGUAGES_IN_HISTORY) {
            recentlyUsedLanguages.add(language);
            setRecentlyUsedLanguages(recentlyUsedLanguages);
            return;
        }


        for (int i = MAX_LANGUAGES_IN_HISTORY - 1; i > 0; --i)
            recentlyUsedLanguages.set(i, recentlyUsedLanguages.get(i - 1));

        recentlyUsedLanguages.set(0, language);
        setRecentlyUsedLanguages(recentlyUsedLanguages);
    }

}
