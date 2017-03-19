package com.licht.ytranslator.data;

import com.google.gson.JsonObject;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.Result;
import com.licht.ytranslator.data.model.TranslateType;
import com.licht.ytranslator.data.sources.AppPreferences;
import com.licht.ytranslator.data.sources.CacheData;
import com.licht.ytranslator.data.sources.YandexTranslateAPI;
import com.licht.ytranslator.utils.LocalizationUtils;

import java.util.ArrayList;
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

    private Localization[] mLocalizations = null;
    private String[] mTranslateTypes = null;
    private String mLocalSymbol = null;

    public DataManager() {
        super();
        YTransApp.getAppComponent().inject(this);

        mLocalSymbol = LocalizationUtils.getCurrentLocalizationSymbol();
        mLocalizations = cacheData.getLanguageList(mLocalSymbol);
        mTranslateTypes = cacheData.getTranslateTypes();
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


    public void cacheLanguageData(List<TranslateType> translateTypes,
                                  List<Localization> localizations) {
        cacheData.saveTranslateType(translateTypes);
        cacheData.saveLocalization(localizations);
    }

    public void localDataIsLoaded(String localConst) {
        appPreferences.putDataCached(localConst, true);
    }

    public String getSourceSymbolLanguage() {
        String sourceLang = appPreferences.getSourceLanguage();
        if ("".equals(sourceLang)) {
            sourceLang = "en";
            setSourceLanguage(sourceLang);
        }

        return sourceLang;
    }

    public String getDestinationSymbolLanguage() {
        String destLang = appPreferences.getDestinationLanguage();
        if ("".equals(destLang)) {
            destLang = "ru";
            setDestinationLanguage(destLang);
        }

        return destLang;
    }

    public String getSourceLanguage() {
        String sourceLang = getSourceSymbolLanguage();
        return getLanguageName(sourceLang);
    }

    public String getDestinationLanguage() {
        String destLang = getDestinationSymbolLanguage();
        return getLanguageName(destLang);
    }

    public ArrayList<String> getSourceLanguageList() {

        final ArrayList<String> list = new ArrayList<>();
        for (Localization localization : mLocalizations)
            list.add(localization.getLangMeaning());

        return list;
    }

    public ArrayList<String> getDestinationLanguageList() {
        final ArrayList<String> result = new ArrayList<>();

        for (String translateType : mTranslateTypes)
            if (translateType.startsWith(mLocalSymbol)) {
                final String destLangSym = translateType.split("-")[1];
                result.add(getLanguageName(destLangSym));
            }

        return result;
    }

    public void setSourceLanguage(String langSym) {
        appPreferences.putSourceLanguage(langSym);
    }

    public void setDestinationLanguage(String langSym) {
        appPreferences.putDestinationLanguage(langSym);
    }

    private String getLanguageName(String transSymbol) {
        return cacheData.getTransMeaning(mLocalSymbol, transSymbol);
    }

    public String getLangugeSymbolByName(String languageName) {
        for (Localization localization : mLocalizations)
            if (localization.getLangMeaning().equals(languageName))
                return localization.getLangSymbol();

        return "";
    }

//    private String getLanguageSym(String localSymbol, String)

}
