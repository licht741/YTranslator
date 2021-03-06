package com.licht.ytranslator.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Обёртка над примерами использованиями фразы.
 * Получаются через API Яндекс Словаря.
 */
public class ExampleObject extends RealmObject {
    private StringWrapper phrase;
    private RealmList<StringWrapper> translates;

    public ExampleObject() {
        super();
    }

    public ExampleObject(StringWrapper phrase, RealmList<StringWrapper> translates) {
        this.phrase = phrase;
        this.translates = translates;
    }

    public StringWrapper getPhrase() {
        return phrase;
    }

    public void setPhrase(StringWrapper phrase) {
        this.phrase = phrase;
    }

    public RealmList<StringWrapper> getTranslates() {
        return translates;
    }

    public void setTranslates(RealmList<StringWrapper> translates) {
        this.translates = translates;
    }
}
