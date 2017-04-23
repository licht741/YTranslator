package com.licht.ytranslator;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.licht.ytranslator.di.component.AppComponent;
import com.licht.ytranslator.di.component.DaggerAppComponent;
import com.licht.ytranslator.di.module.ApplicationModule;
import com.licht.ytranslator.di.module.BusinessModule;
import com.licht.ytranslator.di.module.DataModule;
import com.licht.ytranslator.di.module.NetworkModule;

public class YTransApp extends Application {
    private static YTransApp instance;

    private AppComponent appComponent;

    public static YTransApp get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Для построения приложения используется Dependency Injection и библиотека Dagger.
        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .businessModule(new BusinessModule())
                .networkModule(new NetworkModule())
                .dataModule(new DataModule()).build();
    }

    public static AppComponent getAppComponent() {
        return get().appComponent;
    }
}
