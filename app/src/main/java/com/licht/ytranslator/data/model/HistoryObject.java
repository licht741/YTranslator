package com.licht.ytranslator.data.model;

import io.realm.RealmObject;

/**
 * Обёртка над закэшированным переводом текста с указанным направлением перевода
 */
public class HistoryObject extends RealmObject {
    private String word;
    private String translate;
    private String direction;
    // True, если перевод попал в историю переводов. Иначе False.
    private boolean inHistory;
    // True, если перевод был добавлен в избранное. Иначе False
    private boolean isFavorites;

    public HistoryObject() {
        super();
    }

    public HistoryObject(String word,
                         String translate,
                         String direction,
                         boolean inHistory,
                         boolean isFavorites) {
        this.word = word;
        this.translate = translate;
        this.direction = direction;
        this.isFavorites = isFavorites;
        this.inHistory = inHistory;
    }

    public boolean isInHistory() {
        return inHistory;
    }

    public void setInHistory(boolean inHistory) {
        this.inHistory = inHistory;
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
