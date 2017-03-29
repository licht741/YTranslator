package com.licht.ytranslator.presenters;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.SupportedTranslation;
import com.licht.ytranslator.ui.LoadingScreen.ILoadingView;
import com.licht.ytranslator.utils.LocalizationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoaderPresenter implements IPresenter<ILoadingView> {

    @Inject
    DataManager dataManager;

    private ILoadingView view;

    public LoaderPresenter() {
        super();
        YTransApp.getAppComponent().inject(this);
    }

    @Override
    public void bindView(ILoadingView iLoadingView) {
        view = iLoadingView;
    }

    public void requestData() {
        final String localConst = LocalizationUtils.getCurrentLocalizationSymbol();
        final boolean isDataCached = dataManager.isDataForLocalizationCached(localConst);
        if (isDataCached)
            view.finishLoading();
        else {
            dataManager.loadDataForLocalization(localConst).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject json = response.body();
                    cacheData(json, localConst);
                    dataManager.setDataForLocalizationIsCached(localConst);
                    view.finishLoading();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void checkCache() {
        final int LIMIT = 10;
        final int size = dataManager.getCacheSize();
        if (size > LIMIT) {
            dataManager.clearCache();
        }
    }

    @Override
    public void unbindView() {
        view = null;
    }

    private void cacheData(JsonObject object, String localizationConst) {
        JsonArray dirs = object.getAsJsonArray("dirs");

        final List<SupportedTranslation> types = new ArrayList<>();
        for (int i = 0; i < dirs.size(); ++i)
            types.add(new SupportedTranslation(dirs.get(i).getAsString()));

        final List<Localization> localizationList = new ArrayList<>();

        JsonObject langs = object.getAsJsonObject("langs");
        for (Map.Entry<String, JsonElement> entry : langs.entrySet()) {
            Localization localization = new Localization(localizationConst, entry.getKey(), entry.getValue().getAsString());
            localizationList.add(localization);
        }

        dataManager.cacheLanguageData(types, localizationList);
    }
}
