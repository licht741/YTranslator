package com.licht.ytranslator.presenters;

import android.util.Log;

import com.google.gson.JsonObject;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.Dictionary;
import com.licht.ytranslator.data.model.HistoryItem;
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
    }

    @Override
    public void bindView(ITranslateView iTranslateView) {
        view = iTranslateView;
    }

    @Override
    public void unbindView() {
        view = null;
    }

    private String mCurrentWord;
    private String lang;
    private String translate;

    public void translate(String text) {
        mCurrentWord = text;
        translate = "";
        final String key = YTransApp.get().getString(R.string.key_translate);

        dataManager.requestTranslation(key, text, lang).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                translate = response.body().text.get(0);
                view.setTranslatedText(translate);

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
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

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void addWordToHistory(){
        dataManager.addWordToHistory(new HistoryItem(mCurrentWord, translate, lang, false));
    }

    public void onWordStarred() {
        dataManager.addWordToHistory(new HistoryItem(mCurrentWord, translate, lang, true));

    }

    public String getSourceLanguage() {
        return dataManager.getSourceLanguage();
    }

    public void dictionaryOper() {
        view.openDictionary(mCurrentWord, lang);
    }

    public ArrayList<String> getSourceLanguages() {
        return dataManager.getSourceLanguageList();
    }

    public ArrayList<String> getDestinationLanguages() {
        return dataManager.getDestinationLanguageList();
    }

    public void requestData() {
        updateLanguagePair();
        view.setLanguagePair(getSourceLanguage(), getDestinationLanguage());
    }

    public void updateSourceLanguage(String languageName) {
        final String langSymbol = dataManager.getLanguageSymbolByName(languageName);
        dataManager.setSourceLanguageSymbol(langSymbol);
        view.setLanguagePair(languageName, getDestinationLanguage());
        updateLanguagePair();
    }

    public void updateDestinationLanguage(String languageName) {
        final String langSymbol = dataManager.getLanguageSymbolByName(languageName);
        dataManager.setDestinationLanguage(langSymbol);
        view.setLanguagePair(getSourceLanguage(), languageName);
        updateLanguagePair();
    }

    public String getDestinationLanguage() {
        return dataManager.getDestinationLanguage();
    }

    public void swapLanguages() {
        final String crntSourceLanguage = getSourceLanguage();
        final String crntDestinationLanguage = getDestinationLanguage();

        updateSourceLanguage(crntDestinationLanguage);
        updateDestinationLanguage(crntSourceLanguage);
        updateLanguagePair();
    }

    public void onKeyboardHide() {
        addWordToHistory();
    }

    private void updateLanguagePair() {
        lang = String.format("%s-%s",
                dataManager.getSourceLanguageSymbol(), dataManager.getDestinationLanguageSymbol());
    }
}
