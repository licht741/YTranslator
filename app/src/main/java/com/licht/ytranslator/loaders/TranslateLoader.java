package com.licht.ytranslator.loaders;

import android.util.Log;

import com.google.gson.JsonObject;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.data.model.Result;
import com.licht.ytranslator.data.model.Word;
import com.licht.ytranslator.data.model.WordObject;
import com.licht.ytranslator.presenters.OnTranslateResultListener;
import com.licht.ytranslator.utils.DictionaryAnswerParser;

import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateLoader {
    private DataManager mDataManager;

    private OnTranslateResultListener mListener;

    public TranslateLoader(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void setOnTranslateResultListener(OnTranslateResultListener listener) {
        mListener = listener;
    }

    private static final String TAG = TranslateLoader.class.getSimpleName();

    /**
     * Обращается к API Яндекс переводчика, для получения перевода.
     * При получении результата, передаёт его листенеру.
     *
     * @param key       ключ API
     * @param text      переводимый текст
     * @param direction направление перевода
     */
    public void translate(String key, String text, String direction) {

        Log.e(TAG, "translate: " + key + " " + text + " " + direction);

        // Сначала проверяем, был ли этот запрос закеширован
        final HistoryObject historyObject = mDataManager.getHistoryWord(text, direction);
        if (historyObject != null && mListener != null) {
            mListener.onTranslateResult(historyObject);
            return;
        }

        // Результат закэширован не был, поэтому обращаемся к API
        mDataManager.requestTranslation(key, text, direction).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                final int code = response.body().code; // todo check code

                final String result = response.body().text.get(0);
                HistoryObject historyObject = new HistoryObject(text, result, direction, false, false);
                mDataManager.addWordToHistory(historyObject);

                if (mListener != null)
                    mListener.onTranslateResult(historyObject);

                Log.e(TAG, "mDataManager.requestTranslation: " + key + " " + text + " " + direction);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                // todo
            }
        });
    }

    /**
     * Обращается к API Яндекс словаря, для получения детального объяснения слова.
     * При получении результата, передаёт его листенеру
     *
     * @param key       ключ API
     * @param text      переводимый текст
     * @param direction направление перевода
     */
    public void getDictionaryMeanings(String key, String text, String direction) {
        Log.e(TAG, "getDictionaryMeanings: " + key + " " + text + " " + direction);

        // Если нашли закешированный результат, возвращаем его
        final Word word = mDataManager.getCachedWord(text, direction);
        if (word != null && mListener != null)
            mListener.onDictionaryResult(word);

        mDataManager.getDataFromDictionary(key, text, direction).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // todo check result

                RealmList<WordObject> dicts = DictionaryAnswerParser.parse(response.body());
                Word w = new Word(text, direction, dicts);
                mDataManager.cacheDictionaryWord(w);

                Log.e(TAG, "mDataManager.getDataFromDictionary: " + key + " " + text + " " + direction);

                if (mListener != null)
                    mListener.onDictionaryResult(w);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // todo
            }
        });
    }
}
