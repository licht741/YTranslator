package com.licht.ytranslator.data.model;

import io.realm.RealmObject;

public class Localization extends RealmObject {
    String locale;
    String langSymbol;
    String langMeaning;

    public Localization() {
    }

    public Localization(String locale, String langSymbol, String langMeaning) {
        this.locale = locale;
        this.langSymbol = langSymbol;
        this.langMeaning = langMeaning;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLangSymbol() {
        return langSymbol;
    }

    public void setLangSymbol(String langSymbol) {
        this.langSymbol = langSymbol;
    }

    public String getLangMeaning() {
        return langMeaning;
    }

    public void setLangMeaning(String langMeaning) {
        this.langMeaning = langMeaning;
    }
}
