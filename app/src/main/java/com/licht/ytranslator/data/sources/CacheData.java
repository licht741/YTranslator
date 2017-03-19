package com.licht.ytranslator.data.sources;

import android.util.Log;

import com.facebook.stetho.Stetho;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.Dictionary;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.SupportedTranslation;
import com.licht.ytranslator.data.model.Translate;
import com.licht.ytranslator.data.model.Word;
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

    public void saveTranslateType(List<SupportedTranslation> types) {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(SupportedTranslation.class);
        realm.copyToRealm(types);
        realm.commitTransaction();
    }

    public String[] getTranslateTypes() {
        final Realm realm = Realm.getDefaultInstance();
        RealmResults<SupportedTranslation> results = realm.where(SupportedTranslation.class).findAll();

        String[] translateTypes = new String[results.size()];
        for (int i = 0; i < results.size(); ++i)
            translateTypes[i] = results.get(i).getTranslation();

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
                .equalTo("locale", localizations.get(0).getLanguageSymbol())
                .count() > 0)
            realm.delete(Localization.class);

        realm.copyToRealm(localizations);
        realm.commitTransaction();
    }

    public String getTransMeaning(String localSymbol, String transSymbol) {
        final Realm realm = Realm.getDefaultInstance();
        Localization l = realm.where(Localization.class)
                .equalTo("locale", localSymbol)
                .equalTo("languageSymbol", transSymbol)
                .findFirst();
        if (l == null) {
            Log.e("CacheData", "getTransMeaning: localSymbol: " + localSymbol + " transSymbol: " + transSymbol);
        }

        String r = l.getLanguageTitle();
        return r;
    }

    public Word getCachedDictionary(String word, String dir) {
        final Realm realm = Realm.getDefaultInstance();
        Word w = realm.where(Word.class)
                .equalTo("word", word)
                .equalTo("direction", dir)
                .findFirst();

        return w;
    }

    public void cacheDictionary(Word word) {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (Dictionary dictionary: word.getDictionaries()) {
            for (Translate translate: dictionary.getTranslates()) {
                realm.copyToRealm(translate.getMeanings());
                realm.copyToRealm(translate.getSynonimes());
                realm.copyToRealm(translate);
            }
            realm.copyToRealm(dictionary);
        }
        realm.copyToRealm(word);
        realm.commitTransaction();
    }

}
