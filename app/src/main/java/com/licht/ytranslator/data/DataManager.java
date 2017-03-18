package com.licht.ytranslator.data;

import com.licht.ytranslator.YTransApp;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;

public class DataManager {
    @Inject
    YandexTranslateAPI yandexTranslateAPI;

    public DataManager() {
        super();
        YTransApp.getAppComponent().inject(this);
    }

    public Call<Result> request(String key, String text, String lang) {
//        final String key = "trnsl.1.1.20170318T151457Z.233174f560c42e3d.b08e19f199a26a1d9e019c96cb0629f00a0f6224";
//        final String text = "Hello";
//        final String lang = "en-ru";
//        Request r = new Request(key, text, lang);

        Map<String, String> mapJSON = new HashMap<>();
        mapJSON.put("key", key);
        mapJSON.put("text", text);
        mapJSON.put("lang", lang);
        return yandexTranslateAPI.translate(mapJSON);
    }
}
