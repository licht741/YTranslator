package com.licht.ytranslator.data.model;

import java.util.List;

public class Translate {
    private List<String> synonimes;
    private List<String> meanings;

    public Translate(List<String> synonimes, List<String> meanings) {
        this.synonimes = synonimes;
        this.meanings = meanings;
    }

    public List<String> getSynonimes() {
        return synonimes;
    }

    public void setSynonimes(List<String> synonimes) {
        this.synonimes = synonimes;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }
}
