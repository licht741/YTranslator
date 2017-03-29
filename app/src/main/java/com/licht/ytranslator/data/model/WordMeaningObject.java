package com.licht.ytranslator.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Объект, хранящий информация об одном из использований слова, вместе с дополнительной информацией
 * (значение слова, синонимы, примеры использования)
 */
public class WordMeaningObject extends RealmObject {

    private RealmList<StringWrapper> synonimes;
    private RealmList<StringWrapper> meanings;
    private RealmList<ExampleObject> exampleObjects;
    private String text;
    private String pos;

    public WordMeaningObject() {
        super();
    }

    public WordMeaningObject(RealmList<StringWrapper> synonimes,
                             RealmList<StringWrapper> meanings,
                             RealmList<ExampleObject> exampleObjects,
                             String text,
                             String pos)
    {
        this.synonimes = synonimes;
        this.meanings = meanings;
        this.exampleObjects = exampleObjects;
        this.text = text;
        this.pos = pos;
    }

    public RealmList<StringWrapper> getSynonimes() {
        return synonimes;
    }

    public void setSynonimes(RealmList<StringWrapper> synonimes) {
        this.synonimes = synonimes;
    }

    public RealmList<StringWrapper> getMeanings() {
        return meanings;
    }

    public void setMeanings(RealmList<StringWrapper> meanings) {
        this.meanings = meanings;
    }

    public RealmList<ExampleObject> getExampleObjects() {
        return exampleObjects;
    }

    public void setExampleObjects(RealmList<ExampleObject> exampleObjects) {
        this.exampleObjects = exampleObjects;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
