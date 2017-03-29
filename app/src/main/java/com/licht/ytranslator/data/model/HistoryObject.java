package com.licht.ytranslator.data.model;

import io.realm.RealmObject;

public class HistoryObject extends RealmObject {
    private String word;
    private String translate;
    private String direction;
    private boolean isFavorites;

    public HistoryObject() {
        super();
    }

    public HistoryObject(String word,
                         String translate,
                         String direction,
                         boolean isFavorites) {
        this.word = word;
        this.translate = translate;
        this.direction = direction;
        this.isFavorites = isFavorites;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isFavorites() {
        return isFavorites;
    }

    public void setFavorites(boolean favorites) {
        isFavorites = favorites;
    }
}
