package com.licht.ytranslator.data.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Dictionary extends RealmObject {
    private String text;
    private String trans;
    private RealmList<Translate> translates;

    public Dictionary() {
        super();
    }

    public Dictionary(String text, String trans, RealmList<Translate> translates) {
        this.text = text;
        this.trans = trans;
        this.translates = translates;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public List<Translate> getTranslates() {
        return translates;
    }

    public void setTranslates(RealmList<Translate> translates) {
        this.translates = translates;
    }
}
