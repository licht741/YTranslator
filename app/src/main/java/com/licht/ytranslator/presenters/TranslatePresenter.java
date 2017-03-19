package com.licht.ytranslator.presenters;

import android.util.Log;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.Result;
import com.licht.ytranslator.ui.TranslateView.ITranslateView;
import com.licht.ytranslator.utils.LocalizationUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

    public void translate(String text) {
        final String key = YTransApp.get().getString(R.string.key);
        final String lang = String.format("%s-%s",
                dataManager.getSourceSymbolLanguage(), dataManager.getDestinationSymbolLanguage());
//        final String lang = "en-ru";

        dataManager.request(key, text, lang).enqueue(new Callback<Result>() {
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
        final String langSymbol = dataManager.getLangugeSymbolByName(languageName);
        dataManager.setSourceLanguage(langSymbol);
        view.setLanguagePair(languageName, getDestinationLanguage());
    }

    public void updateDestinationLanguage(String languageName) {
        final String langSymbol = dataManager.getLangugeSymbolByName(languageName);
        dataManager.setDestinationLanguage(langSymbol);
        view.setLanguagePair(getSourceLanguage(), languageName);
    }

    public String getDestinationLanguage() {
        return dataManager.getDestinationLanguage();
    }


}
