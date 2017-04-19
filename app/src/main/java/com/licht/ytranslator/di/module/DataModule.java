package com.licht.ytranslator.di.module;

import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.endpoint.YandexDictionaryAPI;
import com.licht.ytranslator.data.endpoint.YandexTranslateAPI;
import com.licht.ytranslator.data.sources.CacheData;
import com.licht.ytranslator.data.sources.CachedPreferences;
import com.licht.ytranslator.data.sources.UtilsPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    @Provides
    @Singleton
    DataManager provideDataManager(YandexTranslateAPI yandexTranslateAPI,
                                   YandexDictionaryAPI yandexDictionaryAPI,
                                   CacheData cacheData,
                                   CachedPreferences cachedPreferences) {
        return new DataManager(yandexTranslateAPI, yandexDictionaryAPI, cacheData, cachedPreferences);
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
    UtilsPreferences provideUtilsPreferences() {
        return new UtilsPreferences();
    }
}
