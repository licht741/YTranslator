package com.licht.ytranslator.presenters;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.data.model.Result;
import com.licht.ytranslator.data.model.Word;
import com.licht.ytranslator.data.model.WordObject;
import com.licht.ytranslator.ui.TranslateView.ITranslateView;
import com.licht.ytranslator.utils.DictionaryAnswerParser;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslatePresenter implements IPresenter<ITranslateView> {
    @Inject
    DataManager dataManager;

    private ITranslateView view;

    private String currentText;
    private String language;
    private String translatedText;
    private boolean isStarredWord = false;

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

    public void onTextInput(String text) {
        if ("".equals(text)) {
            setTextToResultView("");
            setStarVisible(false);
            return;
        }
        setStarVisible(true);
        currentText = text;
        translatedText = "";

        isStarredWord = false;
        if (view != null) {
            view.setIsStarredView(false);
            view.detailsAreAvailable(false);
        }

        translateText(text);
    }

    public void initializeData(String text, String direction) {
        this.language = direction;
        dataManager.setSourceLanguageSymbol(language.split("-")[0]);
        dataManager.setDestinationLanguage(language.split("-")[1]);

        final HistoryObject object = dataManager.getHistoryWord(text, language);
        if (view != null) {
            view.setInputText(object.getWord());
           // view.setTranslatedText(object.getTranslate());
        }

//        onTextInput(word);
    }

    public void onClearInput() {
        if (view != null)
            view.detailsAreAvailable(false);
        setTextToInputView("");
        currentText = "";
        setTextToResultView("");
    }

    private void setTranslatingToView(String text) {
        setTextToResultView(text);
        if (dataManager.isStarredWord(text, language))
            star();
        else
            unstar();
    }

    private void translateText(String text) {
        if (text == null || "".equals(text))
            return;

        final HistoryObject object = dataManager.getHistoryWord(text, language);
        if (object != null) {

            final String trans = object.getTranslate();
            setTranslatingToView(trans);
            return;
        }

        final String key = YTransApp.get().getString(R.string.key_translate);
        dataManager.requestTranslation(key, text, language).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                setTranslatingToView(response.body().text.get(0));
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                doOnFailure();
            }
        });

        final String keyDict = YTransApp.get().getString(R.string.key_dictionary);
        dataManager.getDataFromDictionary(keyDict, text, language).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                RealmList<WordObject> dicts = DictionaryAnswerParser.parse(response.body());
                Word w = new Word(text, language, dicts);
                dataManager.cacheDictionaryWord(w);

                if (view != null)
                    view.detailsAreAvailable(dicts.size() > 0);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // TODO ?
            }
        });
    }

    private void doOnFailure() {
        Toast.makeText(YTransApp.get(), "Не удалось получить результаты", Toast.LENGTH_SHORT).show();
    }

    public void onStarredClick() {
        if (isStarredWord) {
            addWordToHistory(false);
            unstar();
        } else {
            addWordToHistory(true);
            star();
        }
    }

    private void setStarVisible(boolean isVisible) {
        view.isStarVisible(isVisible);
    }

    private void star() {
        Log.e("TranslatePreseneter", "star: ");
        if (view == null)
            return;
        isStarredWord = true;
        view.setIsStarredView(isStarredWord);
    }

    private void unstar() {
        Log.e("TranslatePreseneter", "unstar: ");
        if (view == null)
            return;

        isStarredWord = false;
        view.setIsStarredView(isStarredWord);
    }

    private void setTextToResultView(String text) {
        if (view == null)
            return;
        translatedText = text;
        view.setTranslatedText(text);
    }

    private void setTextToInputView(String text) {
        view.setInputText(text);
    }

    private void addWordToHistory(boolean isStarredWord) {
        dataManager.addWordToHistory(
                new HistoryObject(currentText, translatedText, language, isStarredWord));
    }

    public String getSourceLanguage() {
        return dataManager.getSourceLanguage();
    }

    public void onOpenDictionaryClick() {
        if (view == null)
            return;
        view.openDictionary(currentText, language);
    }

    public ArrayList<String> getSourceLanguages() {
        return dataManager.getSourceLanguageList();
    }

    public ArrayList<String> getDestinationLanguages() {
        return dataManager.getDestinationLanguageList();
    }

    public void requestData() {
        if (view == null)
            return;

        updateViewLanguagePair();
        view.setLanguagePair(getSourceLanguage(), getDestinationLanguage());
    }

    public void updateSourceLanguage(String languageName) {
        if (view == null)
            return;

        final String langSymbol = dataManager.getLanguageSymbolByName(languageName);
        dataManager.setSourceLanguageSymbol(langSymbol);

        view.setLanguagePair(languageName, getDestinationLanguage());
        updateViewLanguagePair();
    }

    public void updateDestinationLanguage(String languageName) {
        if (view == null)
            return;

        final String langSymbol = dataManager.getLanguageSymbolByName(languageName);
        dataManager.setDestinationLanguage(langSymbol);
        view.setLanguagePair(getSourceLanguage(), languageName);
        updateViewLanguagePair();
    }

    public String getDestinationLanguage() {
        return dataManager.getDestinationLanguage();
    }

    public void onSwapLanguages() {
        final String currentSourceLanguage = getSourceLanguage();
        final String currentDestinationLanguage = getDestinationLanguage();

        updateSourceLanguage(currentDestinationLanguage);
        updateDestinationLanguage(currentSourceLanguage);

        setTextToInputView(translatedText);
        setTextToResultView("");
        translateText(translatedText);

        updateViewLanguagePair();
    }

    public void onKeyboardHide() {
        addWordToHistory(false);
    }

    private void updateViewLanguagePair() {
        language = String.format("%s-%s",
                dataManager.getSourceLanguageSymbol(),
                dataManager.getDestinationLanguageSymbol());
    }
}
