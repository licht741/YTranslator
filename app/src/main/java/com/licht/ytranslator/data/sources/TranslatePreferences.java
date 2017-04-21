package com.licht.ytranslator.data.sources;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.licht.ytranslator.YTransApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Хранит информацию о последнем переводе
 * (переводимый текст, направление перевода, список недавно использованных языков)
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

    /**
     * @return Список недавно использованных языков
     */
    public ArrayList<String> getRecentlyUsedLanguages() {
        String languages = mSharedPreferences.getString(PREF_RECENTLY_USED_LANGUAGES, null);
        if (languages == null) {
            setRecentlyUsedLanguages(new ArrayList<>());
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(languages.split(";")));
    }

    /**
     * Обновляет список недавно использованных языков
     *
     * @param languages Недавно использованные языки
     */
    private void setRecentlyUsedLanguages(List<String> languages) {
        final StringBuilder buffer = new StringBuilder();

        for (String lang : languages)
            buffer.append(lang).append(";");
        if (languages.size() > 0)
            buffer.deleteCharAt(buffer.length() - 1);

        mSharedPreferences.edit().putString(PREF_RECENTLY_USED_LANGUAGES, buffer.toString()).apply();
    }

    private final int MAX_LANGUAGES_IN_HISTORY = 5;

    /**
     * Обновляет список недавно использованных языков в истории
     * Вызывается, когда у нас был выбран новый язык
     *
     * Обновление и хранение языков ведётся по принципу очереди (FIFO)
     *
     * @param language Выбранный язык
     */
    public void updateRecentlyUsedLanguage(String language) {
        final List<String> recentlyUsedLanguages = getRecentlyUsedLanguages();

        // Обрабатываем исключительную ситуацию, когда у нас записана пустая строка
        // (возможна, если мы пока что не добавляли языков в список недавних)
        if (recentlyUsedLanguages.size() == 1 &&
                recentlyUsedLanguages.get(0).equals("")) {
            setRecentlyUsedLanguages(Collections.singletonList(language));
            return;
        }

        // Обрабатываем ситуацию, когда выбран язык, который уже есть в списке недавно добавленных
        // Просто перемещаем его в начало списка
        final int index = recentlyUsedLanguages.indexOf(language);
        if (index != -1) {

            for (int i = index; i > 0; --i)
                recentlyUsedLanguages.set(i, recentlyUsedLanguages.get(i - 1));
            recentlyUsedLanguages.set(0, language);
            
            setRecentlyUsedLanguages(recentlyUsedLanguages);
            return;
        }

        // Если мы ещё не заполнили максимальное количество недавно использованных языков,
        // то просто добавляем его в начало
        if (recentlyUsedLanguages.size() < MAX_LANGUAGES_IN_HISTORY) {
            recentlyUsedLanguages.add(0, language);
            setRecentlyUsedLanguages(recentlyUsedLanguages);
            return;
        }


        for (int i = MAX_LANGUAGES_IN_HISTORY - 1; i > 0; --i)
            recentlyUsedLanguages.set(i, recentlyUsedLanguages.get(i - 1));

        recentlyUsedLanguages.set(0, language);
        setRecentlyUsedLanguages(recentlyUsedLanguages);
    }
}
