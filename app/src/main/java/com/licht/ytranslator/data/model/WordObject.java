package com.licht.ytranslator.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WordObject extends RealmObject {
    @PrimaryKey
    private long id;
    private String text;
    private String trans;
    private String type;
    private RealmList<WordMeaningObject> wordMeaningObjects;

    public WordObject() {
        super();
    }

    public WordObject(long id,
                      String text,
                      String trans,
                      String type,
                      RealmList<WordMeaningObject> wordMeaningObjects) {
        this.id = id;
        this.text = text;
        this.trans = trans;
        this.type = type;
        this.wordMeaningObjects = wordMeaningObjects;
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

    public RealmList<WordMeaningObject> getWordMeaningObjects() {
        return wordMeaningObjects;
    }

    public void setWordMeaningObjects(RealmList<WordMeaningObject> wordMeaningObjects) {
        this.wordMeaningObjects = wordMeaningObjects;
    }
}
