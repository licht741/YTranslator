package com.licht.ytranslator.ui.LoadingScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.licht.ytranslator.MainActivity;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.TranslateType;
import com.licht.ytranslator.presenters.LoaderPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingScreenActivity extends AppCompatActivity implements ILoadingView {

    @Inject
    DataManager dataManager;

    @Inject
    LoaderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_loading_screen);

        YTransApp.getAppComponent().inject(this);

        presenter.bindView(this);
        presenter.requestData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final String key = getString(R.string.key);
        final String lang = "en-ru";

//        dataManager.getData(key, "en").enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject json = response.body();
//                int x = 3;
//                cacheData(json);
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindView();
    }

    @Override
    public void finishLoading() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void cacheData(JsonObject object) {
        JsonArray dirs = object.getAsJsonArray("dirs");

        final List<TranslateType> types = new ArrayList<>();
        for (int i = 0; i < dirs.size(); ++i)
            types.add(new TranslateType(dirs.get(i).getAsString()));

        final List<Localization> localizationList = new ArrayList<>();

        JsonObject langs = object.getAsJsonObject("langs");
        for (Map.Entry<String, JsonElement> entry: langs.entrySet()) {
            Localization localization = new Localization("en", entry.getKey(), entry.getValue().getAsString());
            localizationList.add(localization);
        }


    }
}
