package com.licht.ytranslator.data.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Translate extends RealmObject {
    private RealmList<StringWrapper> synonimes;
    private RealmList<StringWrapper> meanings;
    private RealmList<Example> examples;
    private String text;
    private String pos;

    public Translate() {
        super();
    }

    public Translate(RealmList<StringWrapper> synonimes,
                     RealmList<StringWrapper> meanings,
                     RealmList<Example> examples,
                     String text,
                     String pos) {
        this.synonimes = synonimes;
        this.meanings = meanings;
        this.examples = examples;
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

    public RealmList<Example> getExamples() {
        return examples;
    }

    public void setExamples(RealmList<Example> examples) {
        this.examples = examples;
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
