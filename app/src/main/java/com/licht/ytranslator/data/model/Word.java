package com.licht.ytranslator.data.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Word extends RealmObject {
    private String word;
    private String direction;
    private RealmList<Dictionary> dictionaries;

    public Word() {
        super();
    }

    public Word(String word, String direction, RealmList<Dictionary> dictionaries) {
        this.word = word;
        this.direction = direction;
        this.dictionaries = dictionaries;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public RealmList<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(RealmList<Dictionary> dictionaries) {
        this.dictionaries = dictionaries;
    }
}
