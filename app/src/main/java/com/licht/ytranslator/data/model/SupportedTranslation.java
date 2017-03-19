package com.licht.ytranslator.data.model;

import io.realm.RealmObject;

/**
 * Обёртка над строкой-возможным направлением перевода
 * Необходима, т.к. Realm не умеет работать с классом String
 */
public class SupportedTranslation extends RealmObject {
    private String translation;

    public SupportedTranslation() {}

    public SupportedTranslation(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
