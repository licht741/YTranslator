package com.licht.ytranslator.data.sources;

import android.util.Log;

import com.facebook.stetho.Stetho;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.TranslateType;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

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
        realm.delete(TranslateType.class);
        realm.copyToRealm(types);
        realm.commitTransaction();
    }

    public String[] getTranslateTypes() {
        final Realm realm = Realm.getDefaultInstance();
        RealmResults<TranslateType> results = realm.where(TranslateType.class).findAll();

        String[] translateTypes = new String[results.size()];
        for (int i = 0; i < results.size(); ++i)
            translateTypes[i] = results.get(i).getType();

        return translateTypes;
    }

    public Localization[] getLanguageList(String localSymbol) {
        final Realm realm = Realm.getDefaultInstance();
        RealmResults<Localization> localizations = realm.where(Localization.class)
                .equalTo("locale", localSymbol)
                .findAll();
        return localizations.toArray(new Localization[localizations.size()]);
    }

    public void saveLocalization(List<Localization> localizations) {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (realm.where(Localization.class)
                .equalTo("locale", localizations.get(0).getLangSymbol())
                .count() > 0)
            realm.delete(Localization.class);

        realm.copyToRealm(localizations);
        realm.commitTransaction();
    }

    public String getTransMeaning(String localSymbol, String transSymbol) {
        final Realm realm = Realm.getDefaultInstance();
        Localization l = realm.where(Localization.class)
                .equalTo("locale", localSymbol)
                .equalTo("langSymbol", transSymbol)
                .findFirst();
        if (l == null) {
            Log.e("CacheData", "getTransMeaning: localSymbol: " + localSymbol + " transSymbol: " + transSymbol);
        }

        String r = l.getLangMeaning();
        return r;
    }

}
