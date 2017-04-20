package com.licht.ytranslator.utils;

import java.util.Locale;

public class LocalizationUtils {
    private static final String ENG_LOCALIZATION = "en";
    private static final String RUS_LOCALIZATION = "ru";

    /**
     *
     * Возвращает используемую в приложении локализацию.
     * Если в системе выбрана русская локализация, то возвращает русскую.
     * Во всех остальных случаях возвращает английскую
     * Это связано с тем, что приложение локализовано только на русский и английский
     *
     */
    public static String getCurrentLocalizationSymbol() {
        if ("ru".equals(Locale.getDefault().getLanguage()))
            return RUS_LOCALIZATION;

        return ENG_LOCALIZATION;
    }
}
