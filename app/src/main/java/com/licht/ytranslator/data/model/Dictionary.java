package com.licht.ytranslator.data.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Dictionary extends RealmObject {
    @PrimaryKey
    private long id;
    private String text;
    private String trans;
    private String type;
    private RealmList<Translate> translates;

    public Dictionary() {
        super();
    }

    public Dictionary(long id, String text, String trans, String type, RealmList<Translate> translates) {
        this.id = id;
        this.text = text;
        this.trans = trans;
        this.type = type;
        this.translates = translates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RealmList<Translate> getTranslates() {
        return translates;
    }

    public void setTranslates(RealmList<Translate> translates) {
        this.translates = translates;
    }
}
