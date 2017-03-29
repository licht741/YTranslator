package com.licht.ytranslator.data.sources;

import com.facebook.stetho.Stetho;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.WordObject;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.SupportedTranslation;
import com.licht.ytranslator.data.model.Word;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class CacheData {

    @Inject
    UtilsPreferences utilsPreferences;

    public CacheData() {
        super();
        Realm.init(YTransApp.get());

        YTransApp.getAppComponent().inject(this);

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

        return l.getLanguageTitle();
    }

    public Word getCachedWord(String word, String dir) {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(Word.class)
                .equalTo("word", word)
                .equalTo("direction", dir)
                .findFirst();
    }

    public WordObject getCachedDictionary(long id) {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(WordObject.class)
                .equalTo("id", id)
                .findFirst();
    }

    public void cacheDictionary(Word word) {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(word);
        realm.commitTransaction();
    }

    public void addWordToHistory(HistoryObject item) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            RealmResults<HistoryObject> it = r.where(HistoryObject.class)
                    .equalTo("word", item.getWord()).equalTo("direction", item.getDirection()).findAll();
            if (it.size() > 0) {
                HistoryObject historyObject = it.first();
                historyObject.setFavorites(item.isFavorites());
            }
            else
                r.copyToRealm(item);
        });
    }

    public HistoryObject getWordFromHistory(String word, String direction) {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(HistoryObject.class)
                .equalTo("word", word)
                .equalTo("direction", direction)
                .findFirst();
    }

    public List<HistoryObject> getHistoryWords() {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(HistoryObject.class)
                .findAll();
    }

    public List<HistoryObject> getFavoritesWords() {
        final Realm realm = Realm.getDefaultInstance();
        return realm.where(HistoryObject.class)
                .equalTo("isFavorites", true)
                .findAll();
    }


}
