package com.licht.ytranslator.data.model;

import java.util.List;

/** Обёртка над результатом перевода, возвращаемым с сервера */
public class Result {
    private int code;
    private String lang;
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
