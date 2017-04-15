package com.licht.ytranslator.di.module;

import android.content.Context;

import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.sources.UtilsPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final YTransApp mApp;

    public ApplicationModule(YTransApp app) {
        super();
        mApp = app;
    }

    @Provides
    YTransApp provideApp() {
        return mApp;
    }

    @Provides
    Context provideContext() {
        return mApp;
    }

}
