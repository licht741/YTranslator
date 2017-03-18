package com.licht.ytranslator.di.component;

import com.licht.ytranslator.presenters.LoaderPresenter;
import com.licht.ytranslator.ui.LoadingScreen.LoadingScreenActivity;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.di.module.ApplicationModule;
import com.licht.ytranslator.di.module.BusinessModule;
import com.licht.ytranslator.di.module.NetworkModule;
import com.licht.ytranslator.presenters.TranslatePresenter;
import com.licht.ytranslator.ui.TranslateView.TranslateFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, BusinessModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(TranslateFragment fragment);
    void inject(TranslatePresenter presenter);
    void inject(DataManager manager);
    void inject(LoadingScreenActivity activity);
    void inject(LoaderPresenter presenter);
}
