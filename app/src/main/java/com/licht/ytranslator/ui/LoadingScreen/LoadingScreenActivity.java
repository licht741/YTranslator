package com.licht.ytranslator.ui.LoadingScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.licht.ytranslator.ui.MainActivity;
import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.presenters.LoaderPresenter;

import javax.inject.Inject;

public class LoadingScreenActivity extends AppCompatActivity implements ILoadingView {

    @Inject
    LoaderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        YTransApp.getAppComponent().inject(this);
        setTitle("");
        presenter.bindView(this);
        presenter.requestData();
        presenter.checkCache();
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
        finish();
    }

    @Override
    public void onLoadingFailure() {
        Toast.makeText(this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
        System.exit(0);
    }
}
