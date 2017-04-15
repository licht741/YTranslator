package com.licht.ytranslator.presenters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.SupportedTranslation;
import com.licht.ytranslator.ui.LoadingScreen.ILoadingView;
import com.licht.ytranslator.utils.LocalizationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Загружает данные для работы приложения (локализацию, список языков)
 */
public class LoaderPresenter implements IPresenter<ILoadingView> {

    private ILoadingView view;

    private final DataManager dataManager;

    public LoaderPresenter(DataManager dataManager) {
        super();
        this.dataManager = dataManager;
    }

    @Override
    public void bindView(ILoadingView iLoadingView) {
        view = iLoadingView;
    }

    public void requestData() {
        // Получаем используемую в приложении локализацию.
        // Всего возможно 2 локализации: русская, английская. Это связано с тем, что приложение
        // локализованно только на эти языки
        final String localConst = LocalizationUtils.getCurrentLocalizationSymbol();

        // Если мы уже загрузили данные для выбранной локализации, то больше ничего делать не надо
        final boolean isDataCached = dataManager.isDataForLocalizationCached(localConst);
        if (isDataCached)
            if (view != null) {
                view.finishLoading();
                return;
            }

        // Если данных нет, то начинаем загрузку
        dataManager.loadDataForLocalization(localConst).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response == null || !response.isSuccessful())
                    onLoadingFailure();

                // Кэшируем данные, помечаем, и завершаем загрузку
                JsonObject json = response.body();
                cacheData(json, localConst);
                dataManager.setDataForLocalizationIsCached(localConst);

                if (view != null)
                    view.finishLoading();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (view != null)
                    view.onLoadingFailure();
            }
        });
    }

    /**
     * Вызывается при невозможности загрузки данных (например отсутствие интернета, таймаут, ошибка сервера)
     */
    private void onLoadingFailure() {
        if (view != null)
            view.onLoadingFailure();
    }

    /**
     * Вызывает очистку кэша через DataManager
     */
    public void checkCache() {
        dataManager.clearCacheIfNecessary();
    }

    @Override
    public void unbindView() {
        view = null;
    }

    /**
     * Разбирает полученный JSON и кэширует его в приложении
     *
     * @param object полученный JSON
     * @param localizationConst текущая локализация
     */
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
