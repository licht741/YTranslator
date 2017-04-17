package com.licht.ytranslator.data.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Обёртка над закэшированным переводом текста с указанным направлением перевода
 */
public class HistoryObject extends RealmObject {
    // Пара текст (word) + направление перевода (direction)
    // используется как составной ключ для поиска в БД
    @Index
    private String word;
    @Index
    private String direction;

    private String translate;
    // True, если перевод попал в историю переводов. Иначе False.
    private boolean inHistory;
    // True, если перевод был добавлен в избранное. Иначе False
    private boolean isFavorites;
    // Время кэширования этого перевода
    private Date firstUsingDate;

    public HistoryObject() {
        super();
    }

    public HistoryObject(String word,
                         String translate,
                         String direction,
                         boolean inHistory,
                         boolean isFavorites,
                         Date firstUsingDate) {
        this.word = word;
        this.translate = translate;
        this.direction = direction;
        this.isFavorites = isFavorites;
        this.inHistory = inHistory;
        this.firstUsingDate = firstUsingDate;
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

    public Date getFirstUsingDate() {
        return firstUsingDate;
    }

    public void setFirstUsingDate(Date firstUsingDate) {
        this.firstUsingDate = firstUsingDate;
    }
}
