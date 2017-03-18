package com.licht.ytranslator.di.module;

import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.presenters.TranslatePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BusinessModule {

    @Provides
    @Singleton
    TranslatePresenter provideTranslatePresenter() {
        return new TranslatePresenter();
    }

    @Provides
    @Singleton
    DataManager provideDataManger() {
        return new DataManager();
    }
}
