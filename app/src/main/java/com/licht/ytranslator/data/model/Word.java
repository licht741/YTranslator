package com.licht.ytranslator.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Обёртка над информацией о переводе слова
 */
public class Word extends RealmObject {
    private String word;
    private String direction;
    private RealmList<WordObject> dictionaries;

    public Word() {
        super();
    }

    public Word(String word, String direction, RealmList<WordObject> dictionaries) {
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


    public RealmList<WordObject> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(RealmList<WordObject> dictionaries) {
        this.dictionaries = dictionaries;
    }
}
