package com.licht.ytranslator.data.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Translate extends RealmObject {
    private RealmList<StringWrapper> synonimes;
    private RealmList<StringWrapper> meanings;

    public Translate() {
        super();
    }

    public Translate(RealmList<StringWrapper> synonimes, RealmList<StringWrapper> meanings) {
        this.synonimes = synonimes;
        this.meanings = meanings;
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
}
