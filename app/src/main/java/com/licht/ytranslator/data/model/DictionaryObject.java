package com.licht.ytranslator.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Обёртка над расширенной информацией о переводе слова
 * Получаются через API Яндекс Словаря
 */
public class DictionaryObject extends RealmObject {
    // Word и Direction используются как составной ключ для поиска
    private String word;
    private String direction;

    // Различные значения слова
    private RealmList<WordObject> dictionaries;

    public DictionaryObject() {
        super();
    }

    public DictionaryObject(String word, String direction, RealmList<WordObject> dictionaries) {
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
