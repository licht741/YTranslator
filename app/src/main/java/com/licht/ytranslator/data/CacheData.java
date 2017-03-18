package com.licht.ytranslator.data;

import com.facebook.stetho.Stetho;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.TranslateType;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.List;

import io.realm.Realm;

public class CacheData {

    public CacheData() {
        super();
        Realm.init(YTransApp.get());

        Stetho.initialize(
                Stetho.newInitializerBuilder(YTransApp.get())
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(YTransApp.get()))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(YTransApp.get()).build())
                        .build());
    }

    public void saveTranslateType(List<TranslateType> types) {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(types);
        realm.commitTransaction();
    }

    public void saveLocalization(List<Localization> localizations) {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(localizations);
        realm.commitTransaction();
    }

}
