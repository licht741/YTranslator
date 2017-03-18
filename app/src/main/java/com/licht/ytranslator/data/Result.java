package com.licht.ytranslator.data;

import java.util.List;

public class Result {
    public int code;
    public String lang;
    public List<String> text;

    public Result() {
        super();
    }

    public Result(int code, String lang, List<String> text) {
        this.code = code;
        this.lang = lang;
        this.text = text;
    }
}
