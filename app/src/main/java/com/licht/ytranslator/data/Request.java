package com.licht.ytranslator.data;

public class Request {
    public String key;
    public String text;
    public String lang;

    public Request(String key, String text, String lang) {
        this.key = key;
        this.text = text;
        this.lang = lang;
    }
}
