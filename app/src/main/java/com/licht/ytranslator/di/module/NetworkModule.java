package com.licht.ytranslator.di.module;

import com.licht.ytranslator.data.YandexTranslateAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    YandexTranslateAPI provideYandexTranslateAPI() {
        return provideRetrofit().create(YandexTranslateAPI.class);
    }
}
