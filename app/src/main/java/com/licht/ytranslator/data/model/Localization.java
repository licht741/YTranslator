package com.licht.ytranslator.data.model;

import io.realm.RealmObject;

public class Localization extends RealmObject {
    /** Локализация UI, для которого используется данное название языка */
    private String locale;

    /** Принятый в API символьный код язык */
    private String languageSymbol;

    /** Строковое представление языка, которое должно использоваться в данной локализации */
    private String languageTitle;

    public Localization() {
    }

    public Localization(String locale, String languageSymbol, String languageTitle) {
        this.locale = locale;
        this.languageSymbol = languageSymbol;
        this.languageTitle = languageTitle;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLanguageSymbol() {
        return languageSymbol;
    }

    public void setLanguageSymbol(String languageSymbol) {
        this.languageSymbol = languageSymbol;
    }

    public String getLanguageTitle() {
        return languageTitle;
    }

    public void setLanguageTitle(String languageTitle) {
        this.languageTitle = languageTitle;
    }
}
