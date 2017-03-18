package com.licht.ytranslator.data;

import com.google.gson.JsonObject;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.TranslateType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;

public class DataManager {
    @Inject
    YandexTranslateAPI yandexTranslateAPI;

    @Inject
    CacheData cacheData;

    @Inject
    AppPreferences appPreferences;

    public DataManager() {
        super();
        YTransApp.getAppComponent().inject(this);
    }

    public boolean isDataCached(String localization) {
        return appPreferences.getDataCached(localization);
    }

    public Call<JsonObject> loadDataForLocalization(String localization) {
        return yandexTranslateAPI.getData(YTransApp.get().getString(R.string.key), localization);
    }

    public Call<Result> request(String key, String text, String lang) {

        Map<String, String> mapJSON = new HashMap<>();
        mapJSON.put("key", key);
        mapJSON.put("text", text);
        mapJSON.put("lang", lang);
        return yandexTranslateAPI.translate(mapJSON);
    }

    public Call<JsonObject> getData(String key, String ui) {
        return yandexTranslateAPI.getData(key, ui);
    }

    public void cacheLanguageData(List<TranslateType> translateTypes,
                                  List<Localization> localizations) {
        cacheData.saveTranslateType(translateTypes);
        cacheData.saveLocalization(localizations);
    }

    public void localDataIsLoaded(String localConst) {
        appPreferences.putDataCached(localConst, true);
    }
}
