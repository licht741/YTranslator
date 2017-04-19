package com.licht.ytranslator.di.module;

import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.sources.TranslatePreferences;
import com.licht.ytranslator.loaders.TranslateLoader;
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
    TranslatePresenter provideTranslatePresenter(DataManager dataManager,
                                                 TranslatePreferences translatePreferences,
                                                 TranslateLoader translateLoader) {
        return new TranslatePresenter(dataManager, translateLoader, translatePreferences);
    }

    @Provides
    @Singleton
    TranslatePreferences provideTranslatePreferences() {
        return new TranslatePreferences();
    }

    @Provides
    @Singleton
    LoaderPresenter provideLoaderPresenter(DataManager dataManager) {
        return new LoaderPresenter(dataManager);
    }

    @Provides
    @Singleton
    HistoryPresenter provideHistoryPresenter(DataManager dataManager) {
        return new HistoryPresenter(dataManager);
    }

    @Provides
    @Singleton
    TranslateLoader provideTranslateLoader(DataManager dataManager) {
        return new TranslateLoader(dataManager);
    }

}
