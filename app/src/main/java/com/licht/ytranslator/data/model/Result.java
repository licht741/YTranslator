package com.licht.ytranslator.data.model;

import java.util.List;

/** Обёртка над результатом перевода, возвращаемым с сервера */
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
