package com.licht.ytranslator.utils;

import java.util.Locale;

public class LocalizationUtils {
    private static final String ENG_LOCALIZATION = "en";
    private static final String RUS_LOCALIZATION = "ru";

    public static String getCurrentLocalizationSymbol() {
        if ("ru".equals(Locale.getDefault().getLanguage()))
            return RUS_LOCALIZATION;

        return ENG_LOCALIZATION;
    }
}
