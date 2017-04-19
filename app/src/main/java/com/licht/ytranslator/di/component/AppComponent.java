package com.licht.ytranslator.di.component;

import com.licht.ytranslator.di.module.ApplicationModule;
import com.licht.ytranslator.di.module.BusinessModule;
import com.licht.ytranslator.di.module.DataModule;
import com.licht.ytranslator.di.module.NetworkModule;
import com.licht.ytranslator.ui.DictionaryView.DictionaryActivity;
import com.licht.ytranslator.ui.DictionaryView.DictionaryFragment;
import com.licht.ytranslator.ui.HistoryView.HistoryListFragment;
import com.licht.ytranslator.ui.HistoryView.StarredListFragment;
import com.licht.ytranslator.ui.LanguageSelectView.SelectLanguageActivity;
import com.licht.ytranslator.ui.LoadingScreen.LoadingScreenActivity;
import com.licht.ytranslator.ui.TranslateView.TranslateFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, BusinessModule.class,
                        NetworkModule.class, DataModule.class})
public interface AppComponent {
    void inject(TranslateFragment fragment);
    void inject(LoadingScreenActivity activity);
    void inject(SelectLanguageActivity activity);
    void inject(DictionaryFragment fragment);
    void inject(DictionaryActivity activity);
    void inject(HistoryListFragment fragment);
    void inject(StarredListFragment fragment);
}
