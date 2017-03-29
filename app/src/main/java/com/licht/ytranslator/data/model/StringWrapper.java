package com.licht.ytranslator.data.model;

import io.realm.RealmObject;

/**
 * Обёртка над строками String.
 * Realm не поддерживает строки как объект БД,
 * поэтому приходится использовать обёртку для хранения строки.
 */
public class StringWrapper extends RealmObject {
    private String content;

    public StringWrapper() {
        super();
    }

    public StringWrapper(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
