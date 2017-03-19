package com.licht.ytranslator.presenters;

import android.util.Log;

import com.google.gson.JsonObject;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.Dictionary;
import com.licht.ytranslator.data.model.Result;
import com.licht.ytranslator.data.model.Word;
import com.licht.ytranslator.ui.TranslateView.ITranslateView;
import com.licht.ytranslator.utils.DictionaryAnswerParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslatePresenter implements IPresenter<ITranslateView> {
    @Inject
    DataManager dataManager;

    private ITranslateView view;

    public TranslatePresenter() {
        super();
        YTransApp.getAppComponent().inject(this);

        Word w = dataManager.getCachedWord("Value", "en-ru");

    }

    @Override
    public void bindView(ITranslateView iTranslateView) {
        view = iTranslateView;
    }

    @Override
    public void unbindView() {
        view = null;
    }


    public void translate(String text) {
        final String key = YTransApp.get().getString(R.string.key_translate);
        final String lang = String.format("%s-%s",
                dataManager.getSourceLanguageSymbol(), dataManager.getDestinationLanguageSymbol());
//        final String lang = "en-ru";

        dataManager.requestTranslation(key, text, lang).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.e("TranslatePresenter", "onResponse: " + response.body().text);
                view.setTranslatedText(response.body().text.get(0));
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                // todo
            }
        });

        final String keyDict = YTransApp.get().getString(R.string.key_dictionary);
        dataManager.getDataFromDictionary(keyDict, text, lang).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int x = 3;
                RealmList<Dictionary> dicts = DictionaryAnswerParser.parse(response.body());

                Word w = new Word(text, lang, dicts);

                dataManager.cacheDictionaryWord(w);

                Log.e("f", "onResponse: ");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    public String getSourceLanguage() {
        return dataManager.getSourceLanguage();
    }

    public ArrayList<String> getSourceLanguages() {
        return dataManager.getSourceLanguageList();
    }

    public ArrayList<String> getDestinationLanguages() {
        return dataManager.getDestinationLanguageList();
    }

    public void requestData() {
        view.setLanguagePair(getSourceLanguage(), getDestinationLanguage());
    }

    public void updateSourceLanguage(String languageName) {
        final String langSymbol = dataManager.getLanguageSymbolByName(languageName);
        dataManager.setSourceLanguageSymbol(langSymbol);
        view.setLanguagePair(languageName, getDestinationLanguage());
    }

    public void updateDestinationLanguage(String languageName) {
        final String langSymbol = dataManager.getLanguageSymbolByName(languageName);
        dataManager.setDestinationLanguage(langSymbol);
        view.setLanguagePair(getSourceLanguage(), languageName);
    }

    public String getDestinationLanguage() {
        return dataManager.getDestinationLanguage();
    }


}
