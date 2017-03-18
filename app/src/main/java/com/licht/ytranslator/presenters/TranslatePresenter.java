package com.licht.ytranslator.presenters;

import android.util.Log;

import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.Result;
import com.licht.ytranslator.ui.TranslateView.ITranslateView;

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
        final String key = "trnsl.1.1.20170318T151457Z.233174f560c42e3d.b08e19f199a26a1d9e019c96cb0629f00a0f6224";
        final String lang = "en-ru";

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
}
