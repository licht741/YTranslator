package com.licht.ytranslator.data.model;

import java.util.List;

public class Word {
    private String word;
    private String direction;
    private List<Dictionary> dictionaries;

    public Word() {
        super();
    }

    public Word(String word, String direction, List<Dictionary> dictionaries) {
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

    public List<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(List<Dictionary> dictionaries) {
        this.dictionaries = dictionaries;
    }
}
