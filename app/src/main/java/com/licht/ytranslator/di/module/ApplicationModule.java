package com.licht.ytranslator.di.module;

import android.content.Context;

import com.licht.ytranslator.YTransApp;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private YTransApp mApp;

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
