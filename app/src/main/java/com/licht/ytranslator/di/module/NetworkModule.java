package com.licht.ytranslator.di.module;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.endpoint.YandexDictionaryAPI;
import com.licht.ytranslator.data.endpoint.YandexTranslateAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    @Provides
    @Singleton
    Retrofit provideTranslateRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(YTransApp.get().getString(R.string.translate_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideDictionaryRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(YTransApp.get().getString(R.string.dictionary_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    YandexTranslateAPI provideYandexTranslateAPI() {
        return provideTranslateRetrofit().create(YandexTranslateAPI.class);
    }

    @Provides
    @Singleton
    YandexDictionaryAPI provideYandexDictionaryAPI() {
        return provideDictionaryRetrofit().create(YandexDictionaryAPI.class);
    }
}
