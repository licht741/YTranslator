package com.licht.ytranslator.data.model;

import io.realm.RealmObject;

public class StringWrapper extends RealmObject {
    String content;

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
