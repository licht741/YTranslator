package com.licht.ytranslator.data.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Example extends RealmObject {
    private StringWrapper phrase;
    private RealmList<StringWrapper> translates;

    public Example() {
        super();
    }

    public Example(StringWrapper phrase, RealmList<StringWrapper> translates) {
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
