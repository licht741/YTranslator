package com.licht.ytranslator.data.model;

import io.realm.RealmObject;

public class TranslateType extends RealmObject {
    private String type;
    public TranslateType() {}

    public TranslateType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
