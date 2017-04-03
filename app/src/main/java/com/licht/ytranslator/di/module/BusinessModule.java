package com.licht.ytranslator.di.module;

import com.licht.ytranslator.data.sources.CachedPreferences;
import com.licht.ytranslator.data.sources.CacheData;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.presenters.HistoryPresenter;
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
    CachedPreferences provideAppPreferences() {
        return new CachedPreferences();
    }

    @Provides
    @Singleton
    LoaderPresenter provideLoaderPresenter() {
        return new LoaderPresenter();
    }

    @Provides
    @Singleton
    HistoryPresenter provideHistoryPresenter() {
        return new HistoryPresenter();
    }

}
