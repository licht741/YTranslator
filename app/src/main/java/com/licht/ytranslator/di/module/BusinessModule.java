package com.licht.ytranslator.di.module;

import com.licht.ytranslator.data.sources.AppPreferences;
import com.licht.ytranslator.data.sources.CacheData;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.presenters.LoaderPresenter;
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

    // Todo remove to another module
    @Provides
    @Singleton
    DataManager provideDataManger() {
        return new DataManager();
    }

    @Provides
    @Singleton
    CacheData provideCacheData() {
        return new CacheData();
    }

    @Provides
    @Singleton
    AppPreferences provideAppPreferences() {
        return new AppPreferences();
    }

    @Provides
    @Singleton
    LoaderPresenter provideLoaderPresenter() {
        return new LoaderPresenter();
    }

}
